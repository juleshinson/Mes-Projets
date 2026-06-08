package labyrinth.service.persistence;

import labyrinth.service.serialization.GameDTO;
import labyrinth.service.serialization.SerializationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SqlPersistenceService {
    private final Connection connection ;
    private final SerializationService serializationService ;

    // liste des ecouteurs pour les notifications de modifications de profils
    private final List<Consumer<UUID>> profileListeners = new ArrayList<>() ;

    public SqlPersistenceService(SerializationService serializationService) {
        this.serializationService = serializationService ;

        try {
            // chemin du répertoire de stockage
            Path dir = Path.of(System.getProperty("user.home"), ".labyrinth") ;
            Files.createDirectories(dir) ; // création du répertoire si nécessaire
            Path db = dir.resolve("labyrinth.db") ; // chemin du fichier de BDD dans le dossier

            // ouverture de la connexion
            connection = DriverManager.getConnection("jdbc:sqlite:" + db.toAbsolutePath()) ;

            // activation des clés étrangères
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON") ;
            }
            initTables() ;
        }catch (SQLException | IOException e) {
            throw new SqlException("Erreur ouverture BDD", e) ;
        }
    }

    // création des tables
    private void initTables() throws SQLException {
        initProfilesTable() ;
        initGamesTable() ;
    }

    private void initProfilesTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE
                    IF NOT EXISTS profiles (
                        id TEXT PRIMARY KEY,
                        name TEXT
                    )
                    """) ;
        }
    }

    private void initGamesTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE
                    IF NOT EXISTS games (
                        id TEXT PRIMARY KEY,
                        timestamp INTEGER,
                        data TEXT
                    )
                    """) ;
        }
    }

    // sauvegarde une partie dans la BDD si la partie existe déjà (meme uuid) on la remplace
    public void saveGame(GameDTO gameDTO) {
        try {
            // on serialize le DTO en json
            String data = serializationService.serialize(gameDTO) ;

            // horodatage POSIX
            long timestamp = Instant.now().toEpochMilli() ;

            // on utilise INSERT OR REPLACE pour gérer les doublons
            try (PreparedStatement statement = connection.prepareStatement("""
                    INSERT OR REPLACE INTO games(id, timestamp, data)
                    VALUES (?, ?, ?)
                    """)) {
                statement.setString(1, gameDTO.getUuid().toString()) ;
                statement.setLong(2, timestamp) ;
                statement.setString(3, data) ;
                statement.executeUpdate() ;
            }
        } catch(SQLException e){
            throw new SqlException("Erreur saveGame", e) ;
        }
    }

    // renvoie la partie sauvegardée la plus récente
    // renvoie null si aucune partie en BDD
    public GameDTO loadLatestGame() {
        try {
            // on prend la partie avec le timestamp le plus grand
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT data FROM games ORDER BY timestamp DESC LIMIT 1") ;
                 ResultSet rs = statement.executeQuery()) {

                // rs.next() avance à la premiere ligne
                if (rs.next()) {
                    String data = rs.getString("data") ;
                    return serializationService.deserialize(data) ;
                }
            }
            // aucune partie trouvée
            return null ;

        }catch (SQLException e){
            throw new SqlException("Erreur loadLatestGame", e) ;
        }
    }


    // crée un nouveau profil en BDD et renvoie l'uuid généré
    public UUID createUserProfile(String name) {
        UUID uuid = UUID.randomUUID() ;
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO profiles(id, name) VALUES (?, ?)")) {
                statement.setString(1, uuid.toString()) ;
                statement.setString(2, name) ;
                statement.executeUpdate() ;
            }
            // on notifie les ecouteurs  nouveau profil créé
            notifierProfileListeners(uuid) ;
            return uuid ;

        }catch(SQLException e) {
            throw new SqlException("Erreur createUserProfile", e) ;
        }
    }

    // renvoie tous les profils en BDD
    public List<UserProfile> getUserProfiles() {
        List<UserProfile> profils = new ArrayList<>() ;
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM profiles") ;
                 ResultSet rs = statement.executeQuery()) {

                // rs.next() avance ligne par ligne
                while (rs.next()) {
                    UserProfile profil = chargerProfil(rs) ;
                    profils.add(profil) ;
                }
            }
            return profils ;

        } catch (SQLException e) {
            throw new SqlException("Erreur getUserProfiles", e) ;
        }
    }

    // renvoie le profil lié a l'uuid - null si pas trouvé
    public UserProfile getUserProfile(UUID userId) {
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM profiles WHERE id = ?")) {
                statement.setString(1, userId.toString()) ;
                ResultSet rs = statement.executeQuery() ;

                if (rs.next()) {
                    return chargerProfil(rs) ;
                }
            }
            // pas trouvé
            return null ;

        } catch (SQLException e) {
            throw new SqlException("Erreur getUserProfile", e) ;
        }
    }

    // met à jour le nom du profil spécifié
    public void setUserName(UUID userId, String name) {
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE profiles SET name = ? WHERE id = ?")) {
                statement.setString(1, name) ;
                statement.setString(2, userId.toString()) ;
                statement.executeUpdate() ;
            }
            // on notifie les ecouteurs - profil modifié
            notifierProfileListeners(userId) ;

        } catch (SQLException e) {
            throw new SqlException("Erreur setUserName", e) ;
        }
    }

    // supprime le profil spécifié
    public void deleteUserProfile(UUID userId) {
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM profiles WHERE id = ?")) {
                statement.setString(1, userId.toString()) ;
                statement.executeUpdate() ;
            }
            // on notifie les ecouteurs - profil supprimé
            notifierProfileListeners(userId) ;

        } catch (SQLException e) {
            throw new SqlException("Erreur deleteUserProfile", e) ;
        }
    }

    // méthode utilitaire pour reconstruire un UserProfile depuis un ResultSet
    private static UserProfile chargerProfil(ResultSet rs) throws SQLException {
        String idStr = rs.getString("id") ;
        String name = rs.getString("name") ;
        UUID uuid = UUID.fromString(idStr) ;
        return new UserProfile(uuid, name) ;
    }

    // pour s'abonner aux modifications de profils
    public void ajouterProfileListener(Consumer<UUID> listener) {
        profileListeners.add(listener) ;
    }

    // notifie tous les ecouteurs avec l'uuid du profil modifié
    private void notifierProfileListeners(UUID uuid) {
        for (Consumer<UUID> listener : profileListeners) {
            listener.accept(uuid) ;
        }
    }

}
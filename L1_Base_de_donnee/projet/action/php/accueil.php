<?php
// Connexion à la base de données
require_once("connect_db.php");

// On récupère tous les livres disponibles
$sql = "SELECT * FROM livre";
$result = mysqli_query($dblink, $sql);
?>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Livres – BIBLIO.com</title>
    <link rel="stylesheet" href="../../style.css">
</head>

<body>
   
    <nav>
        <div class="site">
            <img src="../image/entete.jpg" alt="entete">
            <h1>BIBLIO.com</h1>
        </div>
        <ul>
            <li><a href="reservation.php">Mes réservations</a></li>
            <li><a href="../../index.html">Déconnexion</a></li>
        </ul>
    </nav>


    <section class="contenu">
        <header>
            <?php
            // On démarre la session pour récupérer les infos de l'utilisateur
            session_start();

            // On récupère l'email enregistré dans la session
            $mail = $_SESSION['email'];

            // On récupère les infos de l'utilisateur connecté
            $sql2 = "SELECT * FROM utilisateur WHERE email = '$mail'";
            $result2 = mysqli_query($dblink, $sql2);
            $personne = mysqli_fetch_array($result2);

            // On garde son prénom et son id pour les réutiliser 
            $prenom = $personne['prenom'];
            $id = $personne['id_utilisateur'];

            // On remet ces infos dans la session
            $_SESSION['id_utilisateur'] = $id;
            $_SESSION['prenom'] = $prenom;

            // Message de bienvenue personnalisé
            echo "<h1>Bonjour $prenom ! 👋</h1>";
            ?>
            <p>
                Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés en quelques clics
                et accédez à un vaste catalogue de romans, essais, BD et plus encore.
            </p>
            <h3>Découvrez nos livres</h3>
        </header>

       
        <section class="livres-section">
            <?php
            // S'il y a au moins un livre dans la base
            if (mysqli_num_rows($result) > 0) {
                while ($livre = mysqli_fetch_assoc($result)) {
                    echo "<div class='livre'>";

                    
                    echo "<div class='description'>";

                    // Formulaire pour réserver ce livre
                    echo "<form action='reserver.php' method='POST'>";
                    echo "<input type='hidden' name='images' value='{$livre['images']}'>";
                    echo "<input type='hidden' name='titre' value='{$livre['titre']}'>";
                    echo "<input type='hidden' name='auteur' value='{$livre['auteur']}'>";

                    // On affiche l'image, le titre et l'auteur
                    echo "<img src='{$livre['images']}' alt=''>";
                    echo "<p class='titre'>{$livre['titre']}</p>";
                    echo "<p class='auteur'>{$livre['auteur']}</p>";

                    // Bouton pour réserver
                    echo "<input type='submit' value='Réserver' onclick='confirmation()'>";
                    echo "</form>";

                    echo "</div>";
                    echo "</div>"; 
                }
            } else {
                // Aucun livre trouvé
                echo "<p>Aucun livre trouvé.</p>";
            }

            // On ferme la connexion à la base
            mysqli_close($dblink);
            ?>
        </section>
    </section>

    <footer>
        
    </footer>

    <script type="text/javascript">
        // fonction qui affiche un message quand on clique sur "Réserver"
        function confirmation() {
            alert("Confirmation de votre réservation");
        }
    </script>
</body>
</html>

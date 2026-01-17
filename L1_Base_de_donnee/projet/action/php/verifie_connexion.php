<?php
// On inclut le fichier contenant la connexion à la base de données
require_once("connect_db.php");


$sql = "SELECT * FROM utilisateur";
$result = mysqli_query($dblink, $sql);
?>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="utf-8">
        <title></title>
        <link rel="stylesheet" href="../../style.css"> 
    </head>

    <body>
        
        <nav>
            <div class="site">
                <img src="../image/entete.jpg" alt="entete">
                <h1>BIBLIO.com</h1>
            </div>
            <ul>
                <li><a href="../../index.html">Déconnexion</a></li>
            </ul>
        </nav>

        
        <section class="contenu">
            <header>
                <h1>Bonjour !</h1>
                <p>
                    Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés en quelques clics
                    et accédez à un vaste catalogue de romans, essais, BD et plus encore. Simplifiez vos emprunts,
                    explorez nos nouveautés et laissez-vous guider par vos envies de lecture !
                </p>
                <h3>Bienvenue dans votre bibliothèque en ligne !</h3>
            </header>

            <section class="connect-inscri">
                <?php
                    // On démarre la session pour pouvoir enregistrer les infos de l'utilisateur connecté
                    session_start();

                    // On vérifie que les informations d'identification ont bien été envoyées 
                    if (isset($_GET['mail']) && isset($_GET['mdp'])) {
                        $mail = $_GET['mail'];
                        $mdp = $_GET['mdp'];

                        // On vérifie si un utilisateur existe avec l'email fourni
                        $verif = qdb("SELECT * FROM utilisateur WHERE email = '$mail'");

                        // Si au moins un utilisateur a été trouvé
                        if (mysqli_num_rows($verif) != 0) {
                            
                            $personne = mysqli_fetch_array($verif);
                            $mdp_util = $personne['motdepasse'];

                            // on vérifie si le mot de passe correspond 
                            if ($mdp == $mdp_util) {
                                // On enregistre les infos de l'utilisateur dans la session
                                $_SESSION['email'] = $mail;
                                $_SESSION['mdp'] = $mdp;

                                // Message de succès avec lien vers la page d'accueil
                                echo "<div class='accroche'>";
                                echo "<h2>Accéder à la page d'accueil</h2>";
                                echo "<a href='accueil.php'>Cliquez ici</a>";
                                echo "</div>";
                            } else {
                                // Mot de passe incorrect
                                echo "<div class='accroche'>";
                                echo "<h2>Mot de passe incorrect !</h2>";
                                echo "<a href='../../index.html' id='inscription'>Connexion</a>";
                                echo "</div>";
                            }
                        } else {
                            // Aucun utilisateur trouvé avec cet e-mail
                            echo "<div class='accroche'>";
                            echo "<h2>Vous n'êtes pas inscrit, inscrivez-vous !</h2>";
                            echo "<a href='inscription.php' id='inscription'>Inscription</a>";
                            echo "</div>";
                        }
                    } else {
                        // Les champs requis n'ont pas été fournis
                        echo "Erreur";
                    }
                ?>
            </section>
        </section>

        <!-- Message JS (non utilisé dans le code actuel, tu peux le retirer si inutile) -->
        <script type="text/javascript">
            function reussi() {
                alert("Connexion réussie, bienvenue !");
            }
        </script>
    </body>
</html>

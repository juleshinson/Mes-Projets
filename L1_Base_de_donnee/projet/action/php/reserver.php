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
                Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés en quelques clics et accédez à un vaste catalogue.
            </p>
            <h3>Bienvenue dans votre bibliothéque en ligne !</h3>
        </header>

        <section class="connect-inscri">
            <?php
            // Connexion à la base + démarrage de session
            require_once("connect_db.php");
            session_start();

            // Récupère les infos du formulaire 
            $id_util = $_SESSION['id_utilisateur'];
            $titre = $_POST['titre'];
            $auteur = $_POST['auteur'];
            $image = $_POST['images'];

            // Vérifie si le livre a déjà été réservé
            $verif = qdb("SELECT * FROM reservation WHERE titre = '$titre'");

            if (mysqli_num_rows($verif) == 0) {
                // Si le livre n’est pas réservé, on l’ajoute à la table réservation
                $sql = "INSERT INTO reservation (id_utilisateur, titre, auteur, images)
                        VALUES ('$id_util','$titre','$auteur','$image')";
                
                qdb($sql);

                // Message de confirmation 
                echo "<script type='text/javascript'>"; 
                echo "alert('Confirmation de votre réservation');";
                echo "</script>";

                echo "<div class='accroche'>";
                echo "<h2>Nous avons bien enregistré votre réservation.</h2><br>";
                echo "<p>Vérifiez vos <a href='reservation.php'>Réservations</a></p>";
                echo "</div>";
            } else {
                // Si déjà réservé, on prévient l’utilisateur
                echo "<div class='accroche'>";
                echo "<h2>Vous avez déjà réservé ce livre.</h2><br>";
                echo "<p>Vérifiez vos <a href='reservation.php'>Réservations</a></p>";
                echo "</div>";
            }
            ?>
        </section>
    </section>
</body>
</html>

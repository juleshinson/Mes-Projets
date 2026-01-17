<?php
// On se connecte à la base de données
require_once("connect_db.php");

// On récupère tous les livres 
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

                // On récupère son email depuis la session
                $mail = $_SESSION['email'];

                // On récupère les infos complètes de l'utilisateur en base
                $sql2 = "SELECT * FROM utilisateur WHERE email = '$mail'";
                $result2 = mysqli_query($dblink, $sql2);
                $personne = mysqli_fetch_array($result2);

                // On récupère son prénom et son id
                $prenom = $personne['prenom'];
                $id = $personne['id_utilisateur'];

                // On les garde dans la session pour les utiliser ailleurs
                $_SESSION['id_utilisateur'] = $id;
                $_SESSION['prenom'] = $prenom;

                // Message de bienvenue personnalisé
                echo "<h1>Bonjour $prenom ! 👋</h1>";
            ?>
            <p>
                Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés
                en quelques clics et accédez à un vaste catalogue.
            </p>
            <h3>Découvrez nos livres</h3>
        </header>

        <section class="livres-section">
            <?php
                // On récupère les infos de l'utilisateur depuis la session
                $id_util = $_SESSION['id_utilisateur'];

                // On récupère les données envoyées par le formulaire
                $titre = $_POST['titre'];
                $auteur = $_POST['auteur'];
                $image = $_POST['images'];

                // Suppression de la réservation dans la base de données
                $sql = "DELETE FROM reservation WHERE titre = '$titre' AND auteur = '$auteur'";
                qdb($sql); // Fonction personnalisée qui exécute la requête 

                // On récupère les réservations restantes de l’utilisateur (pas utilisé ici mais sans doute utile ailleurs)
                $sql = "SELECT titre, auteur, images FROM reservation WHERE id_utilisateur = '$id_util'";
                $resultat = qdb($sql);

                // Affiche une alerte JavaScript pour confirmer l'annulation
                echo "<script type='text/javascript'>";
                echo "alert('Confirmation de votre annulation');";
                echo "</script>";

                // Message visuel de confirmation + lien vers la page de réservations
                echo "<div class='accroche'>";
                echo "<h2>Nous avons bien annulé votre réservation.</h2><br>";
                echo "<p>Vérifiez vos <a href='reservation.php'>Réservation</a></p>";
                echo "</div>";
            ?>
        </section>
    </section>
</body>
</html>

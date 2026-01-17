<?php
require_once("connect_db.php"); // On se connecte à la base de données

session_start(); // On démarre la session pour identifier l'utilisateur connecté
$id_util = $_SESSION['id_utilisateur']; // On récupère son identifiant depuis la session

//on récupère tous les livres réservés par cet utilisateur
$sql = "SELECT titre, auteur, images
        FROM reservation
        WHERE reservation.id_utilisateur = '$id_util' ";

$resultat = qdb($sql); //On exécute la requête par la fonction qdb
?>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Mes Réservations</title>
    <link rel="stylesheet" href="../../style.css">
</head>

<body>
<nav>
    <div class="site">
        <img src="../image/entete.jpg" alt="entete">
        <h1>BIBLIO.com</h1>
    </div>
    <ul>
        
        <li><a href="accueil.php">Accueil</a></li>
        <li><a href="../../index.html">Déconnexion</a></li>
    </ul>
</nav>

<section class="contenu">
    <header>
        <p>
            Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés en quelques clics
            et accédez à un vaste catalogue de romans, essais, BD et plus encore.
        </p>
        <h3>Vos réservations</h3>
    </header>

    <section class="livres-section">
        <?php 
            //On parcourt toutes les réservations de l'utilisateur et on les affiche une par une
            while ($livre = mysqli_fetch_assoc($resultat)) {

                echo "<div class='livre'>";

                    echo "<div class='description'>";

                    //On crée un formulaire pour chaque livre avec un bouton "Annuler"
                    echo "<form action='annuler.php' method='POST'>";
                    
                    //On envoie les infos du livre en champs cachés pour traitement dans annuler.php
                    echo "<input type='hidden' name='images' value='{$livre['images']}'>";
                    echo "<input type='hidden' name='titre' value='{$livre['titre']}'>";
                    echo "<input type='hidden' name='auteur' value='{$livre['auteur']}'>";
                    
                    //Affichage de la couverture, du titre et de l’auteur
                    echo "<img src='{$livre['images']}' alt=''>";
                    echo "<p class='titre' name='titre'>{$livre['titre']}</p>";
                    echo "<p class='auteur' name='auteur'>{$livre['auteur']}</p>";
                    
                    //Bouton pour annuler la réservation
                    echo "<input type='submit' id='annuler' value='Annuler' onclick='return confirmerAnnulation();'>";
                    
                    echo "</form>";

                    echo "</div>"; 
                echo "</div>"; 
            }
        ?>
        
    </section>
    
</section>

</body>
</html>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Inscription utilisateur</title>
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
                Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés, explorez nos nouveautés et laissez-vous guider par vos envies de lecture !
            </p>
            <h3>Bienvenue dans votre bibliothèque en ligne !</h3>
        </header>

        <section class="connect-inscri">
            <?php
                // Connexion à la base de données
                require_once("connect_db.php");

                // Récupération des données du formulaire d'inscription
                $nom = $_POST['nom'];
                $prenom = $_POST['prenom'];
                $mdp1 = $_POST['mdp1'];
                $mdp2 = $_POST['mdp2'];
                $mail = $_POST['mail'];

                // on vérifie si les deux mots de passe sont identiques
                if ($mdp1 == $mdp2) {

                    // on vérifie si l'adresse e-mail existe déjà
                    $verif = qdb("SELECT * FROM utilisateur WHERE email = '$mail'");

                    if (mysqli_num_rows($verif) == 0) {
                        // Si l'e-mail n'est pas encore utilisé, on insère le nouvel utilisateur
                        $sql = "INSERT INTO utilisateur (nom, prenom, email, motdepasse)
                                VALUES ('$nom', '$prenom', '$mail', '$mdp1')";
                        
                        qdb($sql);

                        // Message de confirmation d'inscription réussie
                        echo "<div class='accroche'>";
                        echo "<h2>Inscription réussie.</h2>";
                        echo "Revenez à la page de <a href='../../index.html'>connexion</a>";
                        echo "</div>";
                    } else {
                        // Si l'adresse e-mail est déjà utilisée
                        echo "<div class='accroche'>";
                        echo "<h2>Déjà inscrit.</h2>";
                        echo "Revenez à la page de <a href='../../index.html'>connexion</a>";
                        echo "</div>";
                    }

                } else {
                    // Si les mots de passe ne correspondent pas
                    echo "<div class='accroche'>";
                    echo "<h2>Les mots de passe ne correspondent pas.</h2>";
                    echo "Revenez à la page d' <a href='inscription.php'>inscription</a>";
                    echo "</div>";
                }
            ?>
        </section>
    </section>
</body>
</html>

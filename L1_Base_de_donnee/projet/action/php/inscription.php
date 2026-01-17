<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Inscription</title>
    <link rel="stylesheet" href="../../style.css"> 
</head>
<body>

    
    <nav>
        <div class="site">
            <img src="../../action/image/entete.jpg" alt="entete"> 
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
                Plongez dans l'univers des livres sans attendre ! Réservez vos ouvrages préférés en quelques clics.
                Simplifiez vos emprunts, explorez les nouveautés et laissez-vous guider par vos envies !
            </p>
            <h3>Connectez-vous / Inscrivez-vous dès maintenant !</h3>
        </header>

       
        <section class="connect-inscri">

            
            <div class="accroche">
                <h1>Votre bibliothèque en ligne</h1>
                <p>Réservez vos livres maintenant et profitez !</p>
            </div>

            
            <div class="connect">
                <!-- On utilise une fonction JS pour valider les champs avant d'envoyer -->
                <form onsubmit="return verifierFormulaire()" method="POST" action="enregistrer_membre.php">
                    <h1>Inscription</h1>

                    
                    <label for="nom">Nom :</label>
                    <input type="text" name="nom" id="nom" required>
                    <br><br>

                    
                    <label for="prenom">Prénom :</label>
                    <input type="text" name="prenom" id="prenom" required>
                    <br><br>

                    
                    <label for="mail">Mail :</label>
                    <input type="email" name="mail" id="mail" required>
                    <br><br>

                    
                    <label for="mdp1">Mot de passe :</label>
                    <input type="password" name="mdp1" id="mdp1" required>
                    <br><br>

                    
                    <label for="mdp2">Confirmez le mot de passe :</label>
                    <input type="password" name="mdp2" id="mdp2" required>
                    <br><br>

                    
                    <input type="submit" id="connexion" value="Inscription">
                </form>

                <!-- Script JavaScript externe pour vérifier les champs du formulaire -->
                <script src="../js/verif.js"></script>
            </div>
        </section>
    </section>
</body>
</html>

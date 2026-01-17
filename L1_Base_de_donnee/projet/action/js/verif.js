
function verifierFormulaire() {
    // On récupère les éléments du formulaire
    var nom = document.getElementById("nom");
    var prenom = document.getElementById("prenom");
    var mail = document.getElementById("mail");
    var mdp1 = document.getElementById("mdp1");
    var mdp2 = document.getElementById("mdp2");

    var valide = true; // pour savoir si tout est ok

    // On remet à zéro les bordures 
    nom.style.border = "";
    prenom.style.border = "";
    mail.style.border = "";
    mdp1.style.border = "";
    mdp2.style.border = "";

    // Vérifie que chaque champ est bien rempli
    if (nom.value == "") {
        nom.style.border = "2px solid red"; // On met une bordure rouge si vide
        valide = false;
    }

    if (prenom.value == "") {
        prenom.style.border = "2px solid red";
        valide = false;
    }

    if (mail.value == "") {
        mail.style.border = "2px solid red";
        valide = false;
    }

    if (mdp1.value == "") {
        mdp1.style.border = "2px solid red";
        valide = false;
    }

    if (mdp2.value == "") {
        mdp2.style.border = "2px solid red";
        valide = false;
    }

    // Vérifie que les deux mots de passe sont identiques
    if (mdp1.value != "" && mdp2.value != "" && mdp1.value !== mdp2.value) {
        mdp1.style.border = "2px solid red";
        mdp2.style.border = "2px solid red";
        alert("Les mots de passe ne sont pas identiques !");
        return false; // On arrête tout si les mots de passe ne correspondent pas
    }

    // Si un champ est vide, on affiche un message d’erreur
    if (!valide) {
        alert("Veuillez remplir tous les champs correctement !");
        return false;
    }

    // Si tout est bon, on peut envoyer le formulaire
    alert("Formulaire prêt à être envoyé !");
    return true;
}




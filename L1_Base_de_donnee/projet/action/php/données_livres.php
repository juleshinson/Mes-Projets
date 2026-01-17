<?php
require_once("connect_db.php"); // On inclut la connexion à la base de données

//on déclaration d’un tableau contenant une liste de livres
//chaque livre est représenté par un tableau avec le titre, l’auteur et l’image associée
$livres = [
    ["Les Misérables", "Victor Hugo", "../image/livre/miserables.jpg"],
    ["Le Comte de Monte-Cristo", "Alexandre Dumas", "../image/livre/montecristo.jpg"],
    ["Orgueil et préjugés", "Jane Austen", "../image/livre/austen.jpg"],
    ["Le Rouge et le Noir", "Stendhal", "../image/livre/rougeetnoir.jpg"],
    ["Candide", "Voltaire", "../image/livre/candide.jpg"],
    ["La Guerre des mondes", "H.G. Wells", "../image/livre/guerremonde.jpeg"],
    ["Frankenstein", "Mary Shelley", "../image/livre/frankenstein.jpeg"],
    ["Le Horla", "Guy de Maupassant", "../image/livre/horla.jpeg"],
    ["Le Mythe de Sisyphe", "Albert Camus", "../image/livre/sisyphe.png"],
    ["Ainsi parlait Zarathoustra", "Friedrich Nietzsche", "../image/livre/zarathoustra.jpg"],
    ["Les 4 Accords Toltèques", "Don Miguel Ruiz", "../image/livre/accord.jpg"],
    ["L Alchimiste", "Paulo Coelho", "../image/livre/alchimiste.jpeg"],
    ["Persepolis", "Marjane Satrapi", "../image/livre/persepolis.jpg"],
];

//On parcourt tous les livres du tableau
foreach ($livres as $livre){
    // On extrait le titre, l’auteur et l’image pour chaque livre
    [$titre, $auteur, $images] = $livre;

    // On vérifie si le livre est déjà présent dans la base de données
    $verif = qdb("SELECT * FROM livre WHERE titre='$titre' AND auteur='$auteur'");

    if (mysqli_num_rows($verif) == 0) {
        // Si le livre n’existe pas, on l’insère
        $sql = "INSERT INTO livre (titre, auteur, images)
                VALUES ('$titre', '$auteur', '$images')";
        qdb($sql);
        echo "Insertion OK<br>";
    } else {
        // Sinon, on affiche qu’il existe déjà
        echo "Ce livre existe déjà.<br>";
    }
}
?>

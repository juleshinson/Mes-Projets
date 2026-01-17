package modele.carte;

import java.util.*;
import java.util.stream.Stream;

// La classe abstraite PaquetBase représente une base commune pour tous les paquets de cartes.
// Elle implémente l'interface Paquet et fournit une implémentation concrète pour la majorité des opérations courantes sur un ensemble de cartes.
public abstract class PaquetBase implements Paquet {

    // Liste qui contient toutes les cartes du paquet
    // Elle est protégée pour être accessible directement par les classes filles
    protected List<Carte> cartes;

    // Constructeur du paquet
    // Il initialise la liste de cartes comme une liste vide
    public PaquetBase() {
        this.cartes = new ArrayList<>();
    }

    // Elle permet de parcourir le paquet avec une boucle for-each
    @Override
    public Iterator<Carte> iterator() {
        return cartes.iterator();
    }

    // Méthode qui retourne un Stream de cartes
    // Elle permet d'utiliser facilement les streams Java sur le paquet
    public Stream<Carte> cartes() {
        return this.cartes.stream();
    }

    // Méthode qui retourne le nombre de cartes dans le paquet
    // Elle correspond à la taille actuelle de la liste
    public int getNbCartes() {
        return this.cartes.size();
    }

    // Méthode qui permet de consulter une carte à un indice donné
    // Si l'indice est invalide (négatif ou trop grand), on retourne null
    // La carte n'est pas retirée du paquet
    public Carte voirCarte(int indice) {
        if (indice < 0 || indice >= getNbCartes()) {
            return null;
        }
        return cartes.get(indice);
    }

    // Méthode protégée qui permet d'ajouter une ou plusieurs cartes au paquet
    // Après l'ajout, le paquet est trié pour garder un ordre cohérent
    protected void ajouterCartes(Carte... cartes) {
        this.cartes.addAll(Arrays.asList(cartes));
        trier();
    }

    // Méthode protégée qui permet d'ajouter une collection de cartes au paquet
    // Après l'ajout, les cartes sont également triées
    protected void ajouterCartes(Collection<Carte> cartes) {
        this.cartes.addAll(cartes);
        trier();
    }

    // Méthode protégée qui mélange les cartes du paquet de façon aléatoire
    // Elle est utilisée typiquement pour une pioche
    protected void melanger() {
        Collections.shuffle(cartes);
    }

    // Méthode protégée qui trie les cartes du paquet
    protected void trier() {
        Collections.sort(cartes);
    }

    // Méthode protégée qui vide complètement le paquet
    // Toutes les cartes sont retirées de la liste
    protected void vider() {
        cartes.removeAll(cartes);
    }

    // Méthode protégée qui permet de prendre une carte à un indice donné
    // La carte est retirée du paquet et retournée
    // Si l'indice est invalide, on retourne null
    protected Carte prendreCarte(int indice) {
        if (indice < 0 || indice >= getNbCartes()) {
            return null;
        }
        Carte c = voirCarte(indice);
        cartes.remove(indice);
        return c;
    }

    // Méthode protégée qui permet de prendre la première carte du paquet
    // Elle s'appuie sur la méthode prendreCarte
    protected Carte prendrePremiereCarte() {
        return prendreCarte(0);
    }

    // Méthode protégée qui permet de prendre la dernière carte du paquet
    // Elle s'appuie également sur la méthode prendreCarte
    protected Carte prendreDerniereCarte() {
        return prendreCarte(getNbCartes() - 1);
    }
}

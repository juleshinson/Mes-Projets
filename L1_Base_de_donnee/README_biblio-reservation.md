# 📚 BIBLIO.com — Plateforme de réservation de livres

Projet universitaire réalisé en **L1 Informatique** à l'Université de Lorraine (juin 2025).

Application web full-stack permettant à des utilisateurs inscrits de consulter un catalogue de livres et de gérer leurs réservations.

---

## Fonctionnalités

- Inscription et connexion avec gestion de session PHP
- Catalogue de livres avec images, titres et auteurs
- Réservation d'un livre en un clic (avec confirmation JavaScript)
- Consultation et annulation des réservations de l'utilisateur connecté
- Déconnexion sécurisée

---

## Stack technique

| Couche | Technologie |
|--------|-------------|
| Frontend | HTML5, CSS3, JavaScript |
| Backend | PHP 8.2 |
| Base de données | MySQL / MariaDB 10.4 |
| Serveur local | XAMPP (Apache) |

### Schéma de la base de données

```
utilisateur (id_utilisateur PK, nom, prenom, email, motdepasse)
livre       (id_livre PK, titre, auteur, images)
reservation (id_utilisateur FK, titre, auteur, images)
```

---

## Structure du projet

```
projet/
├── index.html                  # Page de connexion
├── style.css                   # Feuille de style globale
├── biblio.sql                  # Script SQL (création + données initiales)
├── Documentation.odt           # Documentation technique
└── action/
    ├── php/
    │   ├── connect_db.php          # Connexion à la base
    │   ├── accueil.php             # Catalogue des livres
    │   ├── inscription.php         # Formulaire d'inscription
    │   ├── enregistrer_membre.php  # Traitement inscription
    │   ├── verifie_connexion.php   # Authentification
    │   ├── reservation.php         # Mes réservations
    │   ├── reserver.php            # Action de réservation
    │   └── annuler.php             # Annulation de réservation
    ├── js/
    │   └── verif.js                # Validation formulaires côté client
    └── image/
        ├── entete.jpg
        └── livre/                  # Couvertures des livres
```

---

## Installation locale

### Prérequis
- XAMPP (ou tout serveur Apache + PHP + MySQL)

### Étapes

```bash
# 1. Cloner le dépôt dans le dossier htdocs de XAMPP
git clone https://github.com/TON_USERNAME/biblio-reservation.git
cd biblio-reservation

# 2. Importer la base de données
# Ouvrir phpMyAdmin → Créer une base "biblio" → Importer biblio.sql

# 3. Configurer la connexion (si besoin)
# Éditer projet/action/php/connect_db.php avec vos identifiants MySQL

# 4. Lancer via le navigateur
# http://localhost/biblio-reservation/projet/index.html
```

---

## Aperçu du catalogue

Le catalogue inclut 13 œuvres classiques et contemporaines :
*Les Misérables, Le Comte de Monte-Cristo, Candide, Frankenstein, Persepolis, L'Alchimiste...*

---

## Auteur

**Tiburce Jules Hinson** — L1 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)

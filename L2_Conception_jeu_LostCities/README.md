# 🗺️ Lost Cities : Implémentation Java (console + bots)

Projet universitaire réalisé en **L2 Informatique** à l'Université de Lorraine (janvier 2026).

Implémentation complète du jeu de cartes **Lost Cities** en Java, jouable en console avec modes **Humain vs Humain**, **Humain vs Bot** et **Bot vs Bot**. Trois stratégies de bot sont disponibles, dont un bot avec heuristique améliorée.

---

## Règles du jeu (rappel)

- Jeu de cartes à 2 joueurs, 5 couleurs d'expédition (Jaune, Blanc, Bleu, Vert, Rouge)
- Chaque couleur contient des cartes Pari (×2, ×3) et des cartes chiffrées (2 à 10)
- Les cartes d'une expédition doivent être posées **en ordre croissant strict**
- Les Paris doivent être posés **avant** toute carte chiffrée
- On peut défausser et piocher dans la pioche principale ou dans une défausse de couleur
- **Règle anti-reprise** : impossible de reprendre immédiatement une carte qu'on vient de défausser
- La partie se termine quand la pioche principale est vide

---

## Architecture MVC

```
projet/src/
├── LostCities.java                     # Point d'entrée + menu de jeu
├── modele/
│   ├── Jeu.java                        # Moteur principal : tours, règles, fin de partie
│   ├── Joueur.java                     # État d'un joueur (main, expéditions)
│   ├── Coup.java                       # Représentation d'un coup (carte + action)
│   ├── TypeCoup.java                   # Enum : JOUER / DÉFAUSSER
│   ├── carte/
│   │   ├── Carte.java                  # Entité carte (couleur, type, valeur)
│   │   ├── Couleur.java                # Enum : JAUNE, BLANC, BLEU, VERT, ROUGE
│   │   ├── TypeCarte.java              # Enum : PARI, valeurs 2-10
│   │   ├── Expedition.java             # Expédition d'une couleur (règles de pose)
│   │   ├── PaquetBase.java             # Génération du paquet de 60 cartes
│   │   ├── Pioche.java / Defausse.java # Pioche principale et défausses
│   │   └── PaquetCouleur.java          # Sous-paquet d'une couleur
│   └── strategie/
│       ├── Strategie.java              # Interface : choisirCoup() + choisirPioche()
│       ├── Humain.java                 # Saisie utilisateur console
│       ├── BotDefausse.java            # Bot basique (défausse systématique)
│       ├── BotMin.java                 # Bot qui joue la carte jouable minimale
│       └── BotMinAmeliore.java         # Bot avec heuristique de défausse scorée
├── Vue/
│   ├── VueJeu.java                     # Interface Vue
│   └── VueJeuCLI.java                  # Affichage console de l'état de jeu
└── controle/
    └── ControleJeu.java                # Boucle de jeu principale
```

---

## Stratégies de bot

| Bot | Comportement |
|-----|-------------|
| `BotDefausse` | Défausse toujours, pioche au hasard |
| `BotMin` | Joue la carte jouable de valeur minimale |
| `BotMinAmeliore` | Joue la carte jouable minimale ; si aucune, défausse selon un score heuristique (valeur + malus PARI + malus carte injouable). Pioche dans une défausse si une carte y est immédiatement jouable |

---

## Lancement

```bash
# Compiler depuis le dossier projet/
javac -d out/production src/**/*.java src/*.java

# Lancer
java -cp out/production LostCities
```

Au démarrage, le menu propose :
- Bot vs Bot
- Humain vs Bot
- Humain vs Humain

---

## Tests unitaires

Des tests JUnit sont inclus pour les composants clés :

```
src/modele/carte/
├── ExpeditionTest.java    # Règles de pose des cartes
├── PiocheTest.java        # Comportement de la pioche
└── DefausseTest.java      # Comportement des défausses
```

---

## Auteur

**Tiburce Jules Hinson** : L2 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)

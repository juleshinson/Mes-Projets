# ♟️ Breakthrough IA — Minimax, Alpha-Beta & Monte-Carlo en Java

Projet universitaire réalisé en **L2 Informatique** à l'Université de Lorraine (avril 2026).

Implémentation d'une IA pour le jeu de plateau **Breakthrough** en Java, avec trois algorithmes de recherche (**Minimax**, **Alpha-Beta**) et trois fonctions d'évaluation (**Naïve**, **Booster**, **Monte-Carlo**). Le projet inclut un comparateur de stratégies et un mode Humain vs IA.

---

## Le jeu Breakthrough

Jeu de plateau 8×N à deux joueurs (Blanc / Noir). Chaque joueur possède 2 rangées de pions au départ. Un pion peut avancer tout droit ou en diagonale, et capturer en diagonale. **Le premier joueur à atteindre la rangée adverse gagne.**

---

## Algorithmes implémentés

### Minimax
Exploration exhaustive de l'arbre de jeu jusqu'à une profondeur donnée. Le joueur maximise son score, l'adversaire le minimise.

### Alpha-Beta (élagage)
Optimisation du Minimax : élagage des branches inutiles via les bornes α et β. Même résultat que Minimax, mais beaucoup plus rapide en pratique.

### Fonctions d'évaluation

| Évaluation | Description |
|---|---|
| `EvaluationNaive` | Différence brute de nombre de pions : `nbPionsJoueur - nbPionsAdversaire` |
| `EvaluationBooster` | Différence de pions **pondérée par l'avancement** : chaque pion vaut 10 pts + bonus de ligne (plus un pion est avancé, plus il vaut) |
| `EvaluationMonteCarlo` | Simule N parties aléatoires depuis l'état courant, retourne un score proportionnel au taux de victoire simulé |

---

## Architecture

```
src/
├── Etat/
│   ├── EtatJeu.java        # État du plateau (plateau + tour blanc/noir) + successeurs
│   ├── Plateau.java        # Grille de jeu
│   ├── Case.java           # Contenu d'une case
│   ├── Action.java         # Coup (ligne, colonne, direction)
│   ├── Joueur.java         # Enum : joueurBlanc / joueurNoir
│   ├── Gagnant.java        # Enum : BLANC / NOIR / AUCUN
│   └── IA.java             # Algorithmes Minimax et Alpha-Beta
├── Evaluations/
│   ├── Evaluation.java          # Interface : eval(EtatJeu, Joueur) → int
│   ├── EvaluationNaive.java     # Comptage de pions
│   ├── EvaluationBooster.java   # Pions pondérés par avancement
│   ├── EvaluationMonteCarlo.java# Simulations aléatoires
│   └── ComparateurIA.java       # Fait jouer deux IA l'une contre l'autre
├── Option/
│   ├── Breakthrough.java           # IA vs IA
│   ├── BreakthroughIA.java         # IA vs IA avec paramètres
│   └── BreakthroughHumainVSIA.java # Humain vs IA (console)
├── MainMenuBreakthrough.java        # Menu principal console
└── Tests/
    ├── TestMiniMax.java
    ├── TestAlphaBeta.java
    ├── TestEvaluationNaive.java
    ├── TestEvaluationBooster.java
    ├── TestEvaluationMonteCarlo.java
    ├── TestTempsComparaison.java    # Benchmark Minimax vs Alpha-Beta
    └── TestComparaisonEval.java     # Tournoi entre fonctions d'évaluation
```

---

## Lancement

```bash
# Compiler
javac -d out src/**/*.java src/*.java

# Lancer le menu principal
java -cp out MainMenuBreakthrough
```

Le menu propose :
- IA vs IA (choix de l'évaluation et de la profondeur)
- Humain vs IA
- Lancer les benchmarks de comparaison

---

## Résultats expérimentaux

Le projet inclut des tests de comparaison (`TestTempsComparaison`, `TestComparaisonEval`) permettant de mesurer :
- Le gain de vitesse d'**Alpha-Beta** vs Minimax pur
- Les performances relatives des 3 fonctions d'évaluation en tournoi

---

## Prérequis

- Java 17+
- Aucune dépendance externe

---

## Auteur

**Tiburce Jules Hinson** — L2 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)

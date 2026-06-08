# 🌀 Labyrinth — Jeu de labyrinthe JavaFX / MVVM

Projet universitaire réalisé en **L2 Informatique** à l'Université de Lorraine (juin 2026).  
Développé en collaboration avec **Précieuse Ahoomey-Zunu**.

Implémentation complète du jeu de plateau **Labyrinthe** (jeu de société Ravensburger) en JavaFX avec une architecture **MVVM** avancée, persistence **SQLite**, sérialisation **JSON**, gestion des profils joueurs et une **IA bot** basée sur Monte-Carlo + BFS.

---

## Fonctionnalités

- **Jeu complet** : 2 à 4 joueurs, tuiles rotatives, trésors à collecter, conditions de victoire
- **Mode bot** : IA jouable automatiquement via une stratégie Monte-Carlo + BFS (distance réelle, pas Manhattan)
- **Sauvegarde automatique** : la partie est sauvegardée en SQLite à la fin de chaque tour
- **Reprise de partie** : chargement automatique de la dernière partie au démarrage
- **Gestion de profils** : création, modification et suppression de profils utilisateurs en BDD
- **Sérialisation JSON** : export/import de l'état complet de la partie via Gson + DTOs
- **Interface JavaFX** : vues réactives avec propriétés observables, mise à jour automatique

---

## Architecture — MVVM

```
src/
├── labyrinth/
│   ├── GameBootstrapper.java          # Point d'entrée : crée Game, attache services
│   ├── GameInitializer.java           # Tente de charger la dernière partie sauvegardée
│   ├── SaveService.java               # Sauvegarde SQLite automatique à chaque fin de tour
│   ├── model/
│   │   ├── Game.java                  # Moteur principal : états, tours, règles, bot thread
│   │   ├── Maze.java                  # Grille de tuiles + logique de déplacement
│   │   ├── Tile.java / TileKind.java  # Tuiles et types
│   │   ├── Player.java / Deck.java    # Joueur et deck de trésors
│   │   ├── GameState.java             # Enum : SETUP / MAZE_MOVE / PLAYER_MOVE / TURN_END / GAME_OVER
│   │   ├── MazeMove.java              # Coup de déplacement du labyrinthe
│   │   └── strategy/
│   │       ├── PlayStrategy.java          # Interface stratégie bot
│   │       └── simulation/
│   │           ├── DefaultPlayStrategy.java   # IA : Monte-Carlo + BFS
│   │           ├── GameSimulation.java        # Simulation rollback-safe du jeu
│   │           └── MazeSimulationAdapter.java # Conversion labyrinthe → graphe BFS
│   ├── service/
│   │   ├── persistence/
│   │   │   └── SqlPersistenceService.java # CRUD SQLite (parties + profils)
│   │   └── serialization/
│   │       ├── JsonSerializationService.java  # Gson + adapters custom
│   │       └── [GameDTO, PlayerDTO, MazeDTO…] # DTOs pour sérialisation
│   ├── viewmodel/
│   │   ├── BaseGameViewModel.java     # ViewModel racine (state, currentPlayer)
│   │   ├── GameViewModel.java
│   │   ├── MazeViewModel.java
│   │   └── [CellVM, TileVM, PlayerVM, SlotVM…]
│   ├── view/
│   │   ├── MainView.java / GameView.java
│   │   ├── BoardView.java / MazeView.java
│   │   └── ProfilesManagerWindow.java
│   └── util/
│       ├── graph/
│       │   ├── Graph.java             # Graphe générique
│       │   ├── Paths.java             # BFS depuis un nœud source
│       │   └── MazeToGraphAdapter.java
│       └── MazeBuilder.java           # Construction du labyrinthe standard
```

---

## Stratégie IA (DefaultPlayStrategy)

L'IA utilise une approche **Monte-Carlo + BFS** en plusieurs étapes :

1. **Génération des coups** : explore tous les `MazeMove` × 4 rotations × destinations accessibles
2. **Scoring BFS** : pour chaque coup, calcule la distance réelle (BFS sur le graphe du labyrinthe) vers le prochain trésor — pénalité 50 si inaccessible
3. **Sélection top-5** : garde les 5 meilleurs coups par score BFS
4. **Ajustement Monte-Carlo** : pour chaque candidat, simule N=10 parties aléatoires jusqu'à profondeur 5, pondère le score avec `α=0.5`
5. **Décision finale** : choisit le coup avec le meilleur score ajusté

---

## Persistence SQLite

La BDD est stockée dans `~/.labyrinth/labyrinth.db` avec deux tables :

```sql
CREATE TABLE profiles (id TEXT PRIMARY KEY, name TEXT);
CREATE TABLE games    (id TEXT PRIMARY KEY, timestamp INTEGER, data TEXT);
```

La partie est sérialisée en JSON et stockée dans `data`. `INSERT OR REPLACE` garantit une seule entrée par UUID de partie.

---

## Prérequis

- Java 17+
- JavaFX 17+
- SQLite JDBC (`sqlite-jdbc`)
- Gson

---

## Lancement (IntelliJ IDEA)

```
1. Ouvrir le projet
2. Ajouter les dépendances (sqlite-jdbc, gson, javafx) via les .xml dans .idea/libraries/
3. Configurer les VM options JavaFX
4. Lancer GameBootstrapper ou la classe Main
```

---

## Auteurs

**Tiburce Jules Hinson** & **Précieuse Ahoomey-Zunu** — L2 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)

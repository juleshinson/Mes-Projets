# 🚀 Space Corridor — Jeu de vaisseau spatial en C

Projet universitaire réalisé en **L1 Informatique** à l'Université de Lorraine (mai 2025).  
Développé en binôme avec **Précieuse Ahoomey-Zunu**.

Jeu vidéo 2D en langage C utilisant la bibliothèque SDL2 : pilotez un vaisseau spatial à travers un couloir d'obstacles et atteignez la ligne d'arrivée sans vous crasher.

---

## Gameplay

- Déplacement horizontal du vaisseau (touches directionnelles)
- Météorites et murs descendent à vitesse croissante
- Tir de missiles pour détruire les obstacles
- Effets sonores : musique de fond, explosion, victoire/défaite
- Condition de victoire : atteindre la ligne d'arrivée

---

## Stack technique

| Élément | Détail |
|---------|--------|
| Langage | C (C99) |
| Rendu graphique | SDL2 |
| Texte à l'écran | SDL2_ttf |
| Audio | SDL2_mixer |
| Documentation | Doxygen |
| Build | Makefile (GCC) |

---

## Structure du projet

```
Projet/
├── main.c              # Boucle principale du jeu
├── world.c / world.h   # Logique du monde (positions, collisions, état)
├── graphiques.c / .h   # Rendu graphique (textures, sprites, refresh)
├── constantes.h        # Constantes globales (tailles, vitesses, dimensions)
├── sdl2-light.c / .h   # Couche d'abstraction SDL2
├── sdl2-ttf-light.c/.h # Couche d'abstraction SDL2_ttf
├── Makefile            # Compilation
├── spacecorridor       # Exécutable compilé (Linux)
└── Presentation/       # Slides de présentation du projet
```

### Constantes clés

```c
#define SCREEN_WIDTH    300   // Largeur de l'écran
#define SCREEN_HEIGHT   480   // Hauteur de l'écran
#define SHIP_SIZE        32   // Taille du vaisseau
#define METEORITE_SIZE   32   // Taille d'une météorite
#define MOVING_STEP      10   // Pas de déplacement horizontal
#define INITIAL_SPEED   2.0   // Vitesse initiale des éléments
#define PAS_VITESSE     0.2   // Incrément d'accélération
```

---

## Compilation et lancement

### Prérequis (Linux/Ubuntu)

```bash
sudo apt install libsdl2-dev libsdl2-ttf-dev libsdl2-mixer-dev
```

### Build

```bash
make
./spacecorridor
```

### Nettoyage

```bash
make clean
```

### Générer la documentation Doxygen

```bash
make doc
# Documentation HTML générée dans html/
```

---

## Architecture — points notables

- **Séparation logique/graphique** : `world.c` gère l'état du jeu (positions, collisions, fin de partie) indépendamment du rendu
- **Gestion des textures** : struct `ressources` centralise toutes les textures SDL + sons
- **Détection de collision** : implémentée manuellement sur les bounding boxes des sprites
- **Documentation Doxygen** : tous les fichiers `.h` sont commentés au format Doxygen

---

## Auteurs

**Tiburce Jules Hinson** & **Précieuse Ahoomey-Zunu** — L1 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)

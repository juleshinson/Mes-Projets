# 🗄️ Structures de données en C : Tableau, Liste chaînée, Hashmap

Projet universitaire réalisé en **L2 Informatique** à l'Université de Lorraine (décembre 2025).

Comparaison de trois implémentations d'une même interface `collection_t` pour stocker et interroger des données de matchs League of Legends issues d'une base **SQLite**. Chaque structure respecte la même interface (`collection.h`) et est testée avec **Valgrind** pour la détection de fuites mémoire.

---

## Objectif

Implémenter et comparer les performances de trois structures de données sur les mêmes requêtes :

| Structure | Fichier | Complexité accès index |
|-----------|---------|----------------------|
| Tableau dynamique | `tab.c` | O(1) |
| Liste chaînée | `list.c` | O(n) |
| Table de hachage | `hashmap.c` | O(1) moyen |

---

## Interface commune (`collection.h`)

Toutes les structures exposent la même API :

```c
collection_t* create_empty_col();               // création
void          add(collection_t* C, void* e);    // ajout
int           search_element(collection_t* C, void* e);
int           get_element_by_index(collection_t* c, int i, void** res);
void          destroy(collection_t* C);         // libération mémoire
int           next(collection_t* c, void** e);  // itérateur
void          reset(collection_t* c);           // réinitialise l'itérateur
```

La fonction de hachage classe les matchs par split (`Winter`→0, `Summer`→1, `Spring`→2, autre→3).

---

## Données — Base SQLite League of Legends

La BDD `bdLol.db` contient des matchs professionnels LoL avec les champs :

```c
struct match {
    int   gamelength;   // durée du match
    int   result;       // 0 = défaite, 1 = victoire
    char* pick1;        // premier champion picked
    char* patch;        // version du jeu
    char* split;        // Winter / Summer / Spring
    char* playerid;
    char* teamid;
    char* gameid;
    char* ban1;         // premier champion banni
    char* champion;     // champion joué
};
```

Le programme charge les 5000 premiers matchs depuis SQLite et mesure les performances des trois requêtes.

---

## Requêtes benchmarkées

```c
// Nombre de matchs où pick1 = "Ziggs"
int nb_matchs_pick1_Ziggs(collection_t* matchs);

// Nombre de matchs joués en split "Winter"
int nb_matchs_winter(collection_t* matchs);

// Accès au match à l'index i
match_t* get_match_from_index(collection_t* matchs, int i);
```

---

## Compilation et tests

Le projet utilise un script bash qui **switche dynamiquement** la typedef dans `collection.h` via `awk` pour compiler avec la bonne structure, puis lance Valgrind.

```bash
# Tester une structure individuellement
chmod +x script.bash
./script.bash tab
./script.bash list
./script.bash hashmap

# Lancer les trois en séquence
chmod +x script_total.bash
./script_total.bash
```

Les résultats (temps d'exécution + rapport Valgrind) sont générés dans `Fichiers_tests/`.

### Exemple de sortie

```
TIME : nb_matchs_pick1_Ziggs: 0.4ms ; nb_matchs_winter: 0.3ms ; get_player: 0.0ms
total heap usage: 52,847 allocs, 52,847 frees, 12,341,234 bytes allocated
no leaks are possible
```

---

## Structure du projet

```
projet/
├── main.c                  # Chargement SQLite + benchmark
├── collection.h            # Interface commune + struct match
├── tab.c                   # Implémentation tableau
├── list.c                  # Implémentation liste chaînée
├── hashmap.c               # Implémentation table de hachage
├── fonctions_communes.c    # Itérateur, requêtes, copie/destroy match
├── sqlite3.c / sqlite3.h   # SQLite amalgamation
├── bdLol.db                # Base de données LoL (~75MB)
├── script.bash             # Test unitaire par structure
├── script_total.bash       # Test des 3 structures en séquence
└── Fichiers_tests/         # Résultats attendus (out/err par structure)
```

---

## Prérequis

- GCC
- Valgrind (Linux)

```bash
# Compilation manuelle (exemple avec hashmap)
gcc -Wall -Wextra sqlite3.c fonctions_communes.c hashmap.c main.c -o main
./main
```

---

## Auteur

**Tiburce Jules Hinson** : L2 Informatique, Université de Lorraine  
[juleshinson1@gmail.com](mailto:juleshinson1@gmail.com)

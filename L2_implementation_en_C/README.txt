# Projet final  : Structures de données

Ce projet compare trois implémentations d’une même structure "collection_t" pour stocker et parcourir des matchs issus d’une base SQLite.

# Structures testées
- tab : implémentation par tableau
- list : implémentation par liste chaînée
- hashmap : implémentation par table de hachage

Chaque implémentation respecte la même interface ("collection.h").

# Contenu du dépôt
- main.c : programme principal
- tab.c, list.c, hashmap.c : implémentations de "collection_t"
- collection.h : interface communes de collection_t
- fonctions_communes.c : fonctions communes aux structures (obtenue grace a "next" et "reset")
- bdLol.db, sqlite3.c, sqlite3.h : accès à la base de données
- script.bash et script_total.bash : banc de tests automatique soit pour tester chaque structure soit tout lancer en même temps
    (Tests des fonctions : nb_matchs_pick1_Ziggs, nb_matchs_winter et get_match_from_index avec VALGRIND)
- Fichiers_tests/ : résultats d’exécution et de valgrind

# Compilation et exécution des tests

Un banc de tests est fourni sous forme de script bash.

Pour lancer les tests avec chaque structure (tab, list, hashmap) :

chmod +x script.bash
./script.bash tab
./script.bash list
./script.bash hashmap

Résultats générés dans out.txt et err.txt et sur la console

Pour lancer tous les tests en même temps(tab, list, hashmap) :

chmod +x script_total.bash
./script_total.bash

Résultats générés dans out_tab/list/hasmap.txt et err_tab/list/hasmap.txt et sur la console

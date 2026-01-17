echo "-------TAB-------"
awk '{gsub(/typedef struct list collection_t/, "typedef struct tab collection_t"); print}' collection.h > tmp && mv tmp collection.h
awk '{gsub(/typedef struct hashmap collection_t/, "typedef struct tab collection_t"); print}' collection.h > tmp && mv tmp collection.h

gcc -Wall -Wextra sqlite3.c fonctions_communes.c tab.c main.c -o main
valgrind ./main > Fichiers_tests/out_tab 2> Fichiers_tests/err_tab

grep "TIME" Fichiers_tests/out_tab
grep "total heap usage" Fichiers_tests/err_tab
grep "indirectly lost" Fichiers_tests/err_tab
grep "no leaks" Fichiers_tests/err_tab
echo


echo "-------LIST-------"
awk '{gsub(/typedef struct tab collection_t/, "typedef struct list collection_t"); print}' collection.h > tmp && mv tmp collection.h
awk '{gsub(/typedef struct hashmap collection_t/, "typedef struct list collection_t"); print}' collection.h > tmp && mv tmp collection.h

gcc -Wall -Wextra sqlite3.c fonctions_communes.c list.c main.c -o main
valgrind ./main > Fichiers_tests/out_list 2> Fichiers_tests/err_list

grep "TIME" Fichiers_tests/out_list
grep "total heap usage" Fichiers_tests/err_list
grep "indirectly lost" Fichiers_tests/err_list
grep "no leaks" Fichiers_tests/err_list
echo


echo "-------HASHMAP-------"
awk '{gsub(/typedef struct list collection_t/, "typedef struct hashmap collection_t"); print}' collection.h > tmp && mv tmp collection.h
awk '{gsub(/typedef struct tab collection_t/, "typedef struct hashmap collection_t"); print}' collection.h > tmp && mv tmp collection.h

gcc -Wall -Wextra sqlite3.c fonctions_communes.c hashmap.c main.c -o main
valgrind ./main > Fichiers_tests/out_hashmap 2> Fichiers_tests/err_hashmap

grep "TIME" Fichiers_tests/out_hashmap
grep "total heap usage" Fichiers_tests/err_hashmap
grep "indirectly lost" Fichiers_tests/err_hashmap
grep "no leaks" Fichiers_tests/err_hashmap
echo

if [ "$1" != "tab" ] && [ "$1" != "list" ] && [ "$1" != "hashmap" ]; then
    echo "$1 n'est pas une structure."
    exit
fi
if [ "$1" = "tab" ]; then
    awk '{gsub(/typedef struct list collection_t/, "typedef struct tab collection_t"); print}' collection.h > tmp && mv tmp collection.h
    awk '{gsub(/typedef struct hashmap collection_t/, "typedef struct tab collection_t"); print}' collection.h > tmp && mv tmp collection.h
    gcc -Wall -Wextra sqlite3.c fonctions_communes.c tab.c main.c -o main
fi
if [ "$1" = "list" ]; then
    awk '{gsub(/typedef struct tab collection_t/, "typedef struct list collection_t"); print}' collection.h > tmp && mv tmp collection.h
    awk '{gsub(/typedef struct hashmap collection_t/, "typedef struct list collection_t"); print}' collection.h > tmp && mv tmp collection.h
    gcc -Wall -Wextra sqlite3.c fonctions_communes.c list.c main.c -o main
fi
if [ "$1" = "hashmap" ]; then
    awk '{gsub(/typedef struct list collection_t/, "typedef struct hashmap collection_t"); print}' collection.h > tmp && mv tmp collection.h
    awk '{gsub(/typedef struct tab collection_t/, "typedef struct hashmap collection_t"); print}' collection.h > tmp && mv tmp collection.h
    gcc -Wall -Wextra sqlite3.c fonctions_communes.c hashmap.c main.c -o main
fi

valgrind ./main > Fichiers_tests/out 2> Fichiers_tests/err
echo "-------$1------- "
grep "TIME" Fichiers_tests/out
grep "total heap usage" Fichiers_tests/err
grep "definitely lost" Fichiers_tests/err
grep "indirectly lost" Fichiers_tests/err
grep "no leaks" Fichiers_tests/err

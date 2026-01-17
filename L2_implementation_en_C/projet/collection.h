#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>


struct match{
    int gamelength;
    int result;
    char* pick1;
    char* patch;
    char* split;
    char* playerid;
    char* teamid;
    char* gameid;
    char* ban1;
    char* champion;
};
typedef struct match match_t;

typedef struct hashmap collection_t;

//fonction communes
void match_cpy(match_t* dst, match_t* src);
int match_cmp(match_t* m1, match_t* m2);
void destroy_aux_match(match_t *m);
void print_collection_t(collection_t *C);
int hash (match_t* m);
int nb_matchs_pick1_Ziggs (collection_t* matchs);
int nb_matchs_winter (collection_t* matchs);
match_t* get_match_from_index(collection_t* matchs, int i);



//list.c, tab.c et hashmap.c
collection_t* create_empty_col();
void add(collection_t* C, void* e);
int search_element(collection_t* C, void* e);
int get_element_by_index(collection_t* c, int i, void** res);
void destroy(collection_t* C);
int next(collection_t* c, void** e);
void reset(collection_t* c);




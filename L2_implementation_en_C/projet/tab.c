#include "collection.h"

struct tab{
    int len;
    int maxLen;
    int position;
    void** tab;
};
typedef struct tab collection_t;


collection_t* create_empty_col(){
    //allouer la memoire pour la structure
    collection_t *c = malloc(sizeof(collection_t));
    //initialement vide
    c->len = 0;
    //taille max initiale
    c->maxLen = 1000;
    //position initiale du curseur
    c->position = 0;
    //allouer la memoire pour le tableau
    c->tab = malloc(sizeof(void*) * c->maxLen);

    return c;
}


void destroy(collection_t* C){
    for(int i = 0; i < C->len; i++){
        destroy_aux_match((match_t*)C->tab[i]);
    }
    free(C->tab);
    free(C);
}

void add(collection_t* C, void* e){

    if(C->len < C->maxLen){
        C->tab[C->len] = e;
        //augmenter la taille du tableau
        C->len++;
    }
    else{//si le tableau est plein
        //on double la taille max du tableau
        C->maxLen *= 2;
        //on alloue un nouveau tableau de taille max precedemment doublé
        C->tab = realloc(C->tab, sizeof(void*) * C->maxLen);
        //on rappelle add maintenant avec un tableau plus grand
        add(C,e);
    }
}

// Recherche un élément dans le tableau et renvoie sa position et 0 sinon
int search_element(collection_t* C, void* e){

    int i = 0;
    while(i < C->len && match_cmp((match_t*)C->tab[i], (match_t*)e) != 0){
        i++;
    }

    return i+1;
}

int get_element_by_index(collection_t* c, int i, void** res){
    if(c == NULL || i < 0 || i >= c->len){
        return 0;
    }
    *res = c->tab[i];
    return 1;
}

//permet de parcourir la collection, renvoie 1 si le parcourt est toujours possible ou 0 sinon
int next(collection_t* c, void** e){
    if(c->position < c->len){
        *e = c->tab[c->position];
        c->position++;
        return 1;
    }
    return 0;
}

//reinitialise la position a la première
void reset(collection_t* c){
    c->position = 0;
}






#include "collection.h"

struct list{
    void* element;
    struct list* next;
};
typedef struct list list_t;

struct hashmap {
    list_t* clef[4];
    int position;
    list_t* cur;
};
typedef struct hashmap collection_t;

// Crée une collection vide
collection_t* create_empty_col(){
    collection_t *c = malloc(sizeof(collection_t));
    for(int i = 0; i < 4; i++){
        c->clef[i] = NULL;
    }
    c->position = 0;
    c->cur = c->clef[0];
    return c;
}

// Ajoute un élément à la map
void add (collection_t* C, void* e){
    int i = hash((match_t*)e);

    list_t* n = malloc(sizeof(list_t));
    n->element = e;
    n->next = NULL;
    if(C->clef[i] == NULL){
        C->clef[i] = n;
    }else{
        n->next = C->clef[i];
        C->clef[i] = n;// on relie la nouvelle cellule
    }
}


// Libère toute la mémoire de la map
void destroy(collection_t* C){
    if(C != NULL){
        for (int i = 0; i < 4; i++){
            list_t* n = C->clef[i];
            while(n != NULL){
                list_t* next = n->next;
                destroy_aux_match((match_t*)n->element);
                free(n);
                n = next;
            }
        }
        free(C);
    }
}

//permet de parcourir la collection, renvoie 1 si le parcourt est toujours possible ou 0 sinon
int next(collection_t* c, void** e){
    while (c->cur == NULL && c->position < 4){
        c->position++; //pour tomber sur une liste non vide
        if(c->position < 4){
            c->cur = c->clef[c->position];
        }
    }
    if(c->position >= 4 || c->cur == NULL){
        return 0;
    }
    *e = c->cur->element;
    c->cur = c->cur->next;
    return 1;
}

//reinitialise la position a la première
void reset(collection_t* c){
    c->position = 0;
    c->cur = c->clef[0];
}

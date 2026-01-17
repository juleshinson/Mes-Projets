#include "collection.h"

struct node{
    void* element;
    struct node* next;
};
typedef struct node node_t;

struct list{
    node_t* first;
    node_t* actuel;
};
typedef struct list collection_t;

// Crée une collection vide
collection_t* create_empty_col(){
    collection_t *c = malloc(sizeof(collection_t));
    c->first = NULL;
    c->actuel = NULL;
    return c;
}

// Ajoute un élément à la fin de la liste
void add (collection_t* C, void* e){
    node_t* n = malloc(sizeof(node_t));
    n->element = e;
    n->next = NULL;
    if(C->first == NULL){
        C->first = n;
        C->actuel = n;
    }else{
        node_t* tmp = C->first;
        while (tmp->next != NULL){
            tmp = tmp->next;
        }
        tmp->next = n;// on relie la nouvelle cellule
    }
}

// Recherche un élément dans la liste et renvoie sa position et 0 sinon
int search_element(collection_t* C, void* e){

    int i = 1;
    node_t* n = C->first;
    while (n != NULL && match_cmp((match_t*)n->element,(match_t*)e) != 0){ //continue le parcourt tant que l'élément n'est pas trouvé
        i++;
        n = n->next;
    }
    if (n == NULL) {
        return 0;
    }
    return i;
}

int get_element_by_index(collection_t* c, int i, void** res){
    if(c == NULL || i < 0){
        return 0;
    }else if (i == 0){
        *res = c->actuel->element;
        return 1;
    }else{
        return get_element_by_index(c, i-1, res);
    }
}

// Libère toute la mémoire de la liste
void destroy(collection_t* C){
    if(C != NULL){
        node_t* n = C->first;
        while(n != NULL){
            node_t* next = n->next;
            destroy_aux_match((match_t*)n->element);
            free(n);
            n = next;
        }
        free(C);
    }

}

//permet de parcourir la collection, renvoie 1 si le parcourt est toujours possible ou 0 sinon
int next(collection_t* c, void** e){
    if(c->actuel != NULL){
        *e = c->actuel->element;
        c->actuel = c->actuel->next;
        return 1;
    }
    return 0;
}

//reinitialise la position a la première
void reset(collection_t* c){
    c->actuel = c->first;
}


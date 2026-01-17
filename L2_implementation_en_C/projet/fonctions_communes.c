#include "collection.h"


void match_cpy(match_t* dst, match_t* src){
    //allouer la memoire a chaque champs des cases pour contenir les caractères a copier
    dst->teamid = malloc(strlen(src->teamid) + 1);
    dst->playerid = malloc(strlen(src->playerid) + 1);
    dst->gameid = malloc(strlen(src->gameid) + 1);
    dst->pick1 = malloc(strlen(src->pick1) + 1);
    dst->split = malloc(strlen(src->split) + 1);
    dst->ban1 = malloc(strlen(src->ban1) + 1);
    dst->champion = malloc(strlen(src->champion) + 1);
    dst->patch = malloc(strlen(src->patch) + 1);
    //on fais la copie de chaque caractères dans son champs
    dst->gamelength = src->gamelength;
    dst->result = src->result;
    strcpy(dst->teamid, src->teamid);
    strcpy(dst->playerid, src->playerid);
    strcpy(dst->gameid, src->gameid);
    strcpy(dst->pick1, src->pick1);
    strcpy(dst->split, src->split);
    strcpy(dst->ban1, src->ban1);
    strcpy(dst->champion, src->champion);
    strcpy(dst->patch, src->patch);
}

int match_cmp(match_t* m1, match_t* m2){
    //retourne 0 si les equipes sont les memes et -1 sinon
    if(strcmp(m1->gameid,m2->gameid) == 0 ){
        return 0;
    }
    return -1;
}

//liberer un match
void destroy_aux_match(match_t *m){
    free(m->playerid);
    free(m->patch);
    free(m->pick1);
    free(m->teamid);
    free(m->gameid);
    free(m->ban1);
    free(m->champion);
    free(m->split);
    free(m);
}

// Affiche tous les éléments de la collection
void print_collection_t(collection_t *C){
    void *e;
    reset(C);  // reinitialise la position au debut
    while(next(C, &e) != 0){    // parcours de la collection
        match_t* m = (match_t*)e;
        printf("ID : %s\n",m->gameid);
    }
}

int nb_matchs_pick1_Ziggs (collection_t* matchs){
    void *e;
    int cpt = 0;
    reset(matchs);  // reinitialise la position au debut
    while(next(matchs, &e) != 0){    // parcours de la collection
        match_t* m = (match_t*)e;
        if (strcmp(m->pick1, "Ziggs") == 0){
            cpt++;
        }
    }
    return cpt;
}

int nb_matchs_winter (collection_t* matchs){
    void *e;
    int cpt = 0;
    reset(matchs);  // reinitialise la position au debut
    while(next(matchs, &e) != 0){    // parcours de la collection
        match_t* m = (match_t*)e;
        if (strcmp(m->split, "Winter") == 0){
            cpt++;
        }
    }
    return cpt;
}

match_t* get_match_from_index(collection_t* matchs, int i){
    void *e;
    int cpt = 0;// pour compter la position
    reset(matchs);// reinitialise la position au debut
    while(next(matchs, &e) != 0){//parcours de la collection joueur
        if(cpt == i){ //lorsque la position sera celle qu'on cherche'
            return (match_t*)e; //on renvoie le match
        }
        cpt++;
    }
    return NULL;
}

int hash (match_t* m){

    if (strcmp (m->split, "Winter") == 0 ){
        return 0;
    }
    if (strcmp (m->split, "Summer") == 0 ){
        return 1;
    }
    if(strcmp (m->split, "Spring") == 0 ){
        return 2;
    }
    return 3; //other
}

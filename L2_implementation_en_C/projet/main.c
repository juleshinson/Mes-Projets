#include "sqlite3.h"
#include "collection.h"

char* verif_sqlite_text(sqlite3_stmt* stmt, int col){
    if (sqlite3_column_type(stmt, col) == SQLITE_NULL){
        return NULL;   // pas de valeur
    }
    char* s = malloc(strlen((char*)sqlite3_column_text(stmt, col)) + 1);
    strcpy(s, (char*)sqlite3_column_text(stmt, col));
    return s;
}


int main(){

    sqlite3* bdd;
    int res = sqlite3_open("bdLol.db", &bdd);
    collection_t* cm = create_empty_col(); //creation de la collection
    match_t* m;
    sqlite3_stmt* reqByte;
    const char* truc;
    int nb = 0;
    char* requete = "select distinct gameid,split,pick1,patch,gamelength,playerid,teamid,ban1,champion,result from match";
    res = sqlite3_prepare(bdd, requete, -1, &reqByte, &truc);
    while((res = sqlite3_step(reqByte)) != SQLITE_DONE){ //remplissage de la structure
        nb++;
        if(nb < 5000){
            m = malloc(sizeof(match_t));

            m->gameid   = verif_sqlite_text(reqByte, 0);
            m->split    = verif_sqlite_text(reqByte, 1);
            m->pick1    = verif_sqlite_text(reqByte, 2);
            m->patch    = verif_sqlite_text(reqByte, 3);
            m->gamelength = sqlite3_column_int(reqByte, 4);
            m->playerid = verif_sqlite_text(reqByte, 5);
            m->teamid   = verif_sqlite_text(reqByte, 6);
            m->ban1     = verif_sqlite_text(reqByte, 7);
            m->champion = verif_sqlite_text(reqByte, 8);
            m->result = sqlite3_column_int(reqByte, 9);

            add(cm , m);
        }
    }

    clock_t t1 = clock();
    printf("Nombre de match dont le pick1 est Ziggs (list): %d\n", nb_matchs_pick1_Ziggs(cm));
    t1 = clock() - t1;


    clock_t t2 = clock();
    printf("Nombre de match dont le split est Winter (list) : %d\n", nb_matchs_winter(cm));
    t2 = clock() - t2;

    clock_t t3 = clock();
    m = get_match_from_index(cm, 1);
    t3 = clock() - t3;
    printf("ID : %s\n",m->gameid);


    printf("TIME : nb_matchs_pick1_Ziggs: %.3fms ; nb_matchs_winter: %.3fms ; get_player: %.3fms\n",(double)t1*1000.0/CLOCKS_PER_SEC,(double)t2*1000.0/CLOCKS_PER_SEC,(double)t3*1000.0/CLOCKS_PER_SEC);

    //print_collection_t(cm);

    destroy(cm);
    //fermeture de la base de donnée
    sqlite3_finalize(reqByte);
    sqlite3_close(bdd);
    return EXIT_SUCCESS;
}

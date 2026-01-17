/**
 * \file world.c
 * \brief partie logique des données du monde
 * \author Jules Hinson / Ahoomey-Zunu Nandi
 * \version 0.2
 * \date 5 avril 2025
*/

#include "world.h"
#include "sdl2-light.h"
#include "sdl2-ttf-light.h"
#include <stdio.h>
#include <stdlib.h>

void init_sprite(sprite_t*sprite,int x,int y,int w,int h){

    sprite->x=x;                    //initialisation de l'abscisse

    sprite->y=y;                    //ordonnée du sprite

    sprite->w=w;                    //largeur du sprite

    sprite->h=h;                    //hauteur du sprite
 
    sprite->make_disappear = 0;     //on l'initialise à 0 car on a pas encore de collision

    sprite->message = 0;            //on l'initialise à 0 car on a pas encore de message affiché

}


void print_sprint(sprite_t* sprite){   //affiche un sprite sur la sortie standart 

    printf("x=%f\n y=%f\n w=%f\n h=%f\n",sprite->x,sprite->y,sprite->w,sprite->h);  //affiche les coordonnées et les dimensions du sprite
}

void init_walls(world_t * world){

    //on initialise des tableaux avec les valeurs données 
    float abscisse[30] = {48, 252, 16, 188, 48, 252, 40, 180, 200, 50, 30, 210, 60, 230, 20, 170, 100, 240, 10, 180, 55, 215, 35, 195, 65, 
        225, 25, 185, 80, 200};

    float ordonne[30] = {0, 0, -320, -320, -640, -640, -960, -960, -1280, -1280, -1600, -1600, -1920, -1920, -2240, -2240, -2560, -2560, 
        -2880, -2880, -3200, -3200, -3520, -3520, -3840, -3840, -4160, -4160, -4480, -4480};

    float largeur[30] = {96, 96, 32, 224, 96, 96, 60, 80, 85, 100, 90, 80, 70, 90, 40, 120, 60, 85, 100, 90, 80, 100, 70, 75, 65, 95,
        60, 90, 85, 100};

    float hauteur[30] = {192, 192, 160, 160, 192, 192, 190, 150, 172, 200, 160, 180, 175, 165, 170, 150, 180, 160, 150, 140, 160, 160, 180, 
        170, 150, 175, 190, 160, 165, 170};

    //grâce à la boucle on initialise chaque mur 
    for (int i = 0; i < 5; i++){
        world->walls[i].x = abscisse[i];      //on initialise chaque coordonnée de chaque mur
        world->walls[i].y = ordonne[i];
        world->walls[i].w = largeur[i];
        world->walls[i].h = hauteur[i];
        world->walls[i].make_disappear = 0;  // pour la visibilité des murs avant une collision
    }
}

void init_data(world_t * world){

    init_sprite(&world->sprite,SCREEN_WIDTH/2, SCREEN_HEIGHT - SHIP_SIZE, SHIP_SIZE, SHIP_SIZE);  //initialisation du vaisseau

    init_sprite(&world->but, 200, -800, SHIP_SIZE, 2);                                            //initialisation du but

    init_sprite(&world->finish_line, SCREEN_WIDTH / 2, -4700 , SCREEN_WIDTH, FINISH_LINE_HEIGHT);  //initialisation de la position de la ligne d'arrivée

    init_sprite(&world->missile,SCREEN_WIDTH/2, SCREEN_HEIGHT - SHIP_SIZE, SHIP_SIZE, SHIP_SIZE);  //initialisation du missile

    init_walls(world); ////initialisation des éléments du mur

    world->gameover = 0;    // initialisation à 0 pour commencer le jeu

    world->vy=INITIAL_SPEED;   //initialisation de la vitesse de la ligne d'arrivée et du mur

    world->debut_temps = SDL_GetTicks(); //on l'initialise au temps de départ obtenu grâce à SDL_GetTicks 

    world->temps_final = 0.0f;  //le temps final est nul pour l'instant

    world->missile.make_disappear = 1;     //on met le make_disappear du missile à 1 pour qu'il soit invisible au début

    world->stop = 1;                       //on met stop à 1 car il n'y a  pas encore de pause

    world->missile.type_message = 0;       // on initialise à 0 car il n'y a pas de message au début

    world->compteur_missile_max = 3;       //on met le compteur de missile max à 3

    world->collision_but = 0;             //on l'initialise à 0 car pas encore de collision 

}


void clean_data(world_t *world){
    /* utile uniquement si vous avez fait de l'allocation dynamique (malloc); la fonction ici doit permettre de libérer la mémoire (free) */
}


int is_game_over(world_t *world){
    return world->gameover;  //Fin du jeu
}


void verif_marge(world_t * world){
    
    if (world->sprite.x + world->sprite.w / 2 > SCREEN_WIDTH) {

        world->sprite.x = SCREEN_WIDTH - world->sprite.w / 2;  // Pour se déplacer à droite

    }
    if (world->sprite.x - world->sprite.w / 2 < 0) {

        world->sprite.x = world->sprite.w / 2;  // Pour se déplacer à gauche

    }
}

int sprites_collide(sprite_t*sp1,sprite_t*sp2){
    
    if (abs(sp1->x - sp2->x) <= (sp1->w + sp2->w)/2  &&  abs(sp1->y - sp2->y) <= (sp1->h + sp2->h)/2){ //on vérifie la condition de collision

        return 1; //si vrai on renvoie 1

    } 
    return 0;  //sinon on renvoie 0
}

void handle_sprites_collision(sprite_t* sp1, sprite_t* sp2, world_t* world){
    
    if(sp2->make_disappear == 0 && sprites_collide(sp1,sp2) == 1){ //si il y a collision
        
        world->vy = 0;  //la vitesse verticale s'annule

        sp1->make_disappear = 1;  //make_disappear devient 1 pour faire disparaitre l'élément concerné
    }
}


void update_walls(world_t * world){

    for (int i = 0; i < 5; i++){

        world->walls[i].y += world->vy;  //on met à jour chaque mur 

    }
}


void tir_missile(world_t *world) {

    if (world->missile.make_disappear == 1) {
        
        world->missile.x = world->sprite.x;                           //on met l'abscisse du missile à la même position que le vaisseau

        world->missile.y = world->sprite.y - world->missile.h / 2;    //on initialise l'ordonnée à une position spécifique

        world->missile.make_disappear = 0;                           //on met à 0 pour permettre l'affichage du missile

    }

    world->missile.y -= INITIAL_SPEED;      //vitesse de montée du missile

    for (int i = 0; i < 5; i++) {

        if (world->walls[i].make_disappear == 0 && sprites_collide(&world->missile, &world->walls[i])) {

            world->missile.make_disappear = 1; // Le missile disparaît après la collision

            world->walls[i].make_disappear = 1; // Le mur disparaît aussi après la collision

            world->missile.choc_missile = 1;    // on met à 1 ppour permettre l'application du son dans graphiques.c 

            break;
        }
    }
}


void update_data(world_t *world){

    world->finish_line.y += world->vy;  //mettre a jour a chaque instant la vitesse vertical de la ligne d'arrivée
 
    verif_marge(world);  //pour appliquer le déplacement

    for (int i = 0; i < 30; i++){
        handle_sprites_collision(&world->sprite, &world->walls[i], world); //pour gérer la collision entre les mur et le vaisseau
    }

    handle_sprites_collision(&world->sprite, &world->finish_line, world); //pour gérer la collision entre le vaisseau et la ligne d'arrivée


    if (world->missile.make_disappear == 0){

        tir_missile(world);                    //si le missile est tiré on appelle la fonction tir_missile
    }

    world->but.y += world->vy;

    if( sprites_collide(&world->sprite,&world->but)){

        world->but.make_disappear = 1;                //on met le make_disappear à 1 pour faire disparaître le but

        if(world->compteur_missile_max  <= 4){        //6 c'est le nombre maximum de missile, missile bonus y compris

            world->compteur_missile_max++;                // on incrémente le nombre de missile 

        }        
        world->collision_but = 1;                    // on met collision_but à 1 pour permettre l'affichage du message et l'application du son dans graphiques.c
    }

    update_walls(world);  //on met à jour le mur
}


void handle_events(SDL_Event *event,world_t *world){

    Uint8 *keystates;

    static int compteur_missile = 0;
 
    while( SDL_PollEvent( event ) ) {

        //Si l'utilisateur a cliqué sur le X de la fenêtre
        if( event->type == SDL_QUIT ) {
            //On indique la fin du jeu
            world->gameover = 1;
        }
       
         //si une touche est appuyée
         if(event->type == SDL_KEYDOWN){
            //si la touche appuyée est la fleche de droite
            if(event->key.keysym.sym == SDLK_RIGHT){
                
                world->sprite.x += MOVING_STEP; //déplace le vaisseau vers la droite
            }
            //si la touche appuyée est la fleche de gauche
            if(event->key.keysym.sym == SDLK_LEFT){
                
                world->sprite.x -= MOVING_STEP; //déplace le vaisseau vers la gauche
            }
            //si la touche appuyée est echap
            if(event->key.keysym.sym == SDLK_ESCAPE){

                world->gameover = 1;    //pour sortir du jeu
            }
            //si la touche appuyée est p
            if (event->key.keysym.sym == SDLK_p) {
                
                world->stop = !world->stop;      //on met world->stop à l'inverse de sa valeur initiale
            }
            
            //si la touche appuyée est la fleche du haut
            if(event->key.keysym.sym == SDLK_UP){

                world->vy += PAS_VITESSE;  //pour accélérer la vitesse du mur et  de la ligne d'arrivée
            }
            //si la touche appuyée est espace
            if(compteur_missile <= world->compteur_missile_max && event->key.keysym.sym == SDLK_SPACE){      //et si on a encore des missiles

                tir_missile(world);                       //on appelle la fonction qui tire les missiles

                compteur_missile++;                       //on incrémente le nombre de missile tiré

                world->missile.type_message ++;           //ainsi que le nombre de messages affichés

                if (world->missile.type_message == 4){    //si c'est égal à 4

                    world->missile.type_message = 0;      //on remet à 0 pour avoir l'effet boucle
                }
            
            }
            //si la touche appuyée est la fleche du bas
            if(event->key.keysym.sym == SDLK_DOWN){

                if(world->vy >= 0){  //Pour s'assurer de ne pas avoir une vitesse négative

                    world->vy -= PAS_VITESSE;  //pour ralentir la vitesse du mur et de la ligne d'arrivée
                }
            }

        }}
    }

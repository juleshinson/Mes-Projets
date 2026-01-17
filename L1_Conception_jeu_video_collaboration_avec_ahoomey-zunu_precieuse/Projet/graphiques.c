/**
 * \file graphiques.c
 * \brief partie graphique des données du monde
 * \author Jules Hinson / Ahoomey-Zunu Nandi
 * \version 0.2
 * \date 5 avril 2025
*/

#include "graphiques.h"
#include <stdio.h>
#include "sdl2-light.h"
#include "sdl2-ttf-light.h"
#include <SDL2/SDL_mixer.h>



void clean_textures(ressources *textures){

    clean_texture(textures->background);    //nettoie textures de l'image de fond

    clean_texture(textures->spaceship);     //nettoie textures du vaisseau

    clean_texture(textures->finish);        //nettoie textures de la ligne d'arrivée

    clean_texture(textures->meteorite);     //nettoie textures des météorites

    clean_font(textures->font);             //nettoie textures du texte affiché

    clean_texture(textures->missile);      //nettoie textures des missiles

    clean_texture(textures->but);          //nettoie la texture du but
}


void  init_textures(SDL_Renderer *renderer, ressources *textures){

    textures->font = load_font("ressources/arial.ttf", 14);                             //initialise textures de la police du texte affiché

    textures->background = load_image( "ressources/space-background.bmp",renderer);     //initialise textures de l'image de fond

    textures->spaceship = load_image("ressources/spaceship.bmp",renderer);              //initialise textures du vaisseau

    textures->finish = load_image("ressources/finish_line.bmp",renderer);               //initialise textures de la ligne d'arrivée

    textures->meteorite = load_image("ressources/meteorite.bmp",renderer);              //initialise textures des météorites

    textures->missile = load_image("ressources/missile.bmp",renderer);                  //initialise textures du missile
    
    textures->but = load_image("ressources/but.bmp",renderer);                          //initialise textures du but

    textures->music = Mix_LoadMUS("ressources/background_sound.mp3");                   //initialise textures de la musique de fond 

    textures->sound_win = Mix_LoadWAV("ressources/goodresult.wav");                     //initialise textures de la musique de victoire
 
    textures->sound_lose = Mix_LoadWAV("ressources/fiasco.wav");                        //initialise textures de la musique de défaite
 
    textures->sound_explosion = Mix_LoadWAV("ressources/explosion.wav");                //initialise textures de la musique d'explosion

    textures->sound_mini_explosion = Mix_LoadWAV("ressources/boom.wav");                //initialise textures de la musique de la mini explosion

    textures->sound_mini_win = Mix_LoadWAV("ressources/mini_win.wav");                  //initialise textures de la musique de la petite victoire

}

void apply_background(SDL_Renderer *renderer, SDL_Texture *texture){

    if(texture != NULL){
      apply_texture(texture, renderer, 0, 0);   //appliquer l'arrière plan
    }
}


void apply_sprite(SDL_Renderer *renderer,SDL_Texture *texture,sprite_t * sprite){

    if(texture != NULL && sprite->make_disappear == 0){  //dans ce cas pas de collision 
        apply_texture(texture, renderer, sprite->x - sprite->w /2, sprite->y - sprite->h /2);     //Affiche un sprite à une position donnée
    }
}


void apply_walls(SDL_Renderer *renderer,SDL_Texture *texture,sprite_t * sprite){

    // Applique la texture à cette position
    for (int i = 0; i < sprite->w / METEORITE_SIZE; i++) {
        for (int j = 0; j < sprite->h / METEORITE_SIZE; j++) {
            apply_texture(texture, renderer, sprite->x + i * METEORITE_SIZE - sprite->w / 2, sprite->y + j * METEORITE_SIZE - sprite->h / 2);  //affiche les murs
        }
    }
}

void if_apply_walls(SDL_Renderer *renderer, SDL_Texture *texture, world_t *world){

    for (int i = 0; i < 30; i++) {

        if (world->walls[i].make_disappear == 0) {               //en absence de collision
            
            apply_walls(renderer, texture, &world->walls[i]);    //on affiche les murs
        }
    }
}

void refresh_graphics(SDL_Renderer *renderer, world_t *world,ressources *textures){
    
    //on vide le renderer
    clear_renderer(renderer);
    
    //application des textures dans le renderer
    apply_background(renderer, textures->background);
   
    //application des textures dans le renderer pour le vaisseau
    apply_sprite(renderer,textures->spaceship, &world->sprite);

    //application des textures dans le renderer pour la ligne d'arrivée
    apply_sprite(renderer,textures->finish, &world->finish_line);

    //application des textures dans le renderer pour le missile
    apply_sprite(renderer,textures->missile, &world->missile);

    apply_sprite(renderer,textures->but, &world->but);

    //application des textures dans le renderer pour les murs si elle n'a pas été touché par un missile
    if_apply_walls(renderer, textures->meteorite, world);  
 
    //un tableau de caractères où on stocke les phrases à afficher
    char* messages[4] = {"","Encore une !", "Tu es en feu !", "Parfait !"};
    
    if(world->missile.make_disappear == 1 && world->missile.choc_missile == 1){

        Mix_PlayChannel(-1, textures->sound_mini_explosion, 0);              //un son pour l'explosion 

        world->missile.choc_missile = 0;                                    //on remet world->missile.choc_missile à 0 pour éviter de repéter sans cesse le bloc

        int debut = SDL_GetTicks();                                         // on récupère le temps du début avec SDL_GetTicks

        int duree = 1000;                                                    // on initialise la durée à 1000ms ou 1s
        
        while (SDL_GetTicks() - debut < duree) {                            
            
            apply_text(renderer, SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 - 100, 200, 75, messages[world->missile.type_message], textures->font); //on affiche tant que la durée est inférieure à 1s

            update_screen(renderer);                     //on met à jour 
        }
        
    }

    world->temps_au_cours = ((SDL_GetTicks() - world->temps_au_cours ) / 1000) - 3;  //calcul du temps affiché sur le jeu a chaque instant (on soustrait 3s a cause du temps de GET READY!)

    sprintf(world->affichage_temps,"TIME: %.d", world->temps_au_cours); //stockage du message de temps 

    apply_text(renderer, 10 , 10, 120, 50, world->affichage_temps, textures->font); //affichage du message
    
    if (world->sprite.make_disappear == 1 ){ //pour exécuter ce bloc uniquement en cas de collision 
       
        if (world->sprite.message == 0){  //on éxecute ce bloc qu'une seule fois si le message n'a pas déjà été affiché

            float fin_temps = SDL_GetTicks();  //on recueille le temps a la collision

            world->temps_final = ((fin_temps - world->debut_temps) / 1000) - 3;  //on lui soustrait le temps du début et on divise par 1000 pour le convertir en seconde
        
            world->debut_temps = fin_temps ;  // on met à jour le début temps pour pouvoir fermer le jeu après 2 secondes après

            //  Affichage d'un message en fonction de la collision avec un mur ou  la ligne d’arrivée

            if (sprites_collide(&world->sprite, &world->finish_line)){  //en cas de collision avec la ligne d'arrivée 

                sprintf(world->message_final,"You finished in %.2f s !", world->temps_final);  //on stocke grâce à sprintf un message de victoire avec le temps calculé plus haut

                Mix_HaltMusic();                                   //on stoppe la musique du fond 

                Mix_PlayChannel(-1, textures->sound_win, 0);       //on met le son de la victoire

            } else { //si on heurte plutôt un mur,

                strcpy(world->message_final,"GAME OVER"); //ce sera un message d'erreur. On utilise strcpy pour copier le message dans notre variable

                Mix_HaltMusic();         // pour stopper la musique de fond immédiatement

                Mix_PlayChannel(-1, textures->sound_explosion, 0); //on met le son de l'explosion

                Mix_PlayChannel(-1, textures->sound_lose, 0);       //suivi de celui de l'echec
            }
            world->sprite.message = 1; //pour empêcher le bloc de s'exécuter plusieurs fois
        }

        apply_text(renderer,SCREEN_WIDTH/2-100, SCREEN_HEIGHT/2-100, 200, 75, world->message_final, textures->font); //enfin on affiche le message sur l'écran selon les cas avec apply_text

        if ((SDL_GetTicks() - world->debut_temps)/ 1000 >= 2) { //on calcule le temps écoulé depuis la fin de la partie si ce temps est supérieur ou égal à 2

            world->gameover = 1 ; //on met gameover à 1 pour entraîner la fermeture du jeu
        }
    }   
   
    if(world->collision_but == 1){

        Mix_PlayChannel(-1, textures->sound_mini_win, 0);                                                    //un son d'encouragement

        apply_text(renderer,SCREEN_WIDTH/2-100, SCREEN_HEIGHT/2-100, 200, 75, " + 2 tirs !", textures->font);   //on court message d'encouragement

        world->collision_but = 0;            // on remet à 0 pour éviter d'entrer constamment dans le if
    }
    update_screen(renderer);       // on met à jour l'écran
}


void init(SDL_Window **window, SDL_Renderer ** renderer, ressources *textures, world_t * world){

    init_ttf();                                                 //initialise l'environnement

    init_sdl(window,renderer,SCREEN_WIDTH, SCREEN_HEIGHT);      //initialise la fenêtre

    if (Mix_OpenAudio(44100, MIX_DEFAULT_FORMAT, 2, 2048) < 0) { // initialisation audio
        fprintf(stderr, "Erreur d'initialisation de SDL_mixer : %s\n", Mix_GetError());  //Message d'erreur
        exit(EXIT_FAILURE);
    }

    init_data(world);                                           //initialise le monde 

    init_textures(*renderer,textures);                          //initialise les differentes textures

    if (Mix_PlayMusic(textures->music, -1) == -1) {      
               // musique en boucle
        fprintf(stderr, "Erreur musique : %s\n", Mix_GetError());  //on affiche un message d'erreur
    }

    Mix_VolumeMusic(MIX_MAX_VOLUME / 6);                         //pour ajuster le volume
}


void clean(SDL_Window *window, SDL_Renderer * renderer, ressources *textures, world_t * world){
 
    clean_data(world);               //nettoie le monde

    clean_textures(textures);        //nettoie les différentes textures

    clean_sdl(renderer,window);      //nettoie la fenêtre

    clean_music(textures->music);    // nettoie la musique de fond

    clean_chunk(textures->sound_explosion); // nettoie la musique de collision

    clean_chunk(textures->sound_lose); // nettoie la musique de defaite

    clean_chunk(textures->sound_win); // nettoie la musique de victoire

    clean_chunk(textures->sound_mini_win);   //nettoie la musique d'encouragement

    Mix_CloseAudio();               //ferme le système audio
}
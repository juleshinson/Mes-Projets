/**
 * \file world.h
 * \brief en-tête de la partie logique des données du monde
 * \author Jules Hinson / Ahoomey-Zunu Nandi
 * \version 0.2
 * \date 5 avril 2025
*/

#ifndef WORLD_H
#define WORLD_H

#include "constantes.h"
#include "sdl2-light.h"
#include "sdl2-ttf-light.h"

/**
 * \brief Coordonnés d'un élément du monde du jeu
*/

struct sprite_s{
  float x; /*!< abscisse du sprite */

  float y; /*!< ordonnée du sprite */

  float h; /*!< hauteur du sprite */

  float w; /*!< largeur du sprite */

  int make_disappear; /*!< pour contrôler la disparition du vaisseau */

  int message;  /*!< pour contrôler l'affichage du message */

  int type_message;

  int choc_missile;
};

/**
 * \brief Type qui correspond aux coordonnées des éléments du jeu
 */
typedef struct sprite_s sprite_t;


/**
 * \brief Représentation du monde du jeu
*/

struct world_s{

  sprite_t sprite;                  /*!< Champ indiquant la structure des coordonnées du vaisseau */

  sprite_t finish_line;             /*!< Champ indiquant la structure des coordonnées de la ligne d'arrivée */

  sprite_t missile;                 /*!< Champ indiquant la structure des coordonnées du missile */

  sprite_t but;                     /*!< Champ indiquant la structure des coordonnées du but*/

  sprite_t walls [30];              /*!< Champ indiquant l'ensemble des murs du jeu */

  float vy;                         /*!< Champ indiquant la vitesse de déplacement de la ligne d'arrivée et du mur de météorites */

  float debut_temps;                /*!< Champ indiquant le temps au lancement du jeu */

  int temps_au_cours;               /*!< Champ indiquant le temps en temps réel*/

  char affichage_temps[150];        /*!< Champ où sera stocké le temps en temps réel */

  float temps_final;                /*!< Champ indiquant le temps a la fin du jeu */

  char message_final[150];          /*!< Champ où sera stocké le message final */

  int gameover;                     /*!< Champ indiquant si l'on est à la fin du jeu */
 
  int stop;                         /*!< Champ indiquant si l'on est en pause*/

  int compteur_missile_max;         /*!< Champ indiquant le nombre de missile */

  int collision_but;                /*!< Champ indiquant s'il y a collision avec le but*/

}; 

/**
 * \brief Type qui correspond aux données du monde
 */
typedef struct world_s world_t; 


/**
 * \brief La fonction qui initialise les coordonnées des éléments
 * \param sprite Un élément du jeu
 * \param x abscisse du sprite
 * \param y oronnéee du sprite
 * \param w largeur du sprite
 * \param h hauteur du sprite
 */

void init_sprite(sprite_t * sprite,int x,int y,int w,int h);


/**
 * \brief La fonction qui affiche les positions du sprite
 * \param sprite Un élement du jeu
 */

void print_sprint(sprite_t* sprite);

/**
 * \brief La fonction initialise les données les murs de météorites
 * \param world les données du monde
 */

 void init_walls(world_t * world);

/**
 * \brief La fonction initialise les données du monde du jeu
 * \param world les données du monde
 */

void init_data(world_t * world);


/**
 * \brief La fonction nettoie les données du monde
 * \param world les données du monde
 */

void clean_data(world_t * world);


/**
 * \brief La fonction indique si le jeu est fini en fonction des données du monde
 * \param world les données du monde
 * \return 1 si le jeu est fini, 0 sinon
 */

int is_game_over(world_t * world);


/**
 * \brief La fonction verifie si on a atteint les marges de l'écran
 * \param world les données du monde
 */

void verif_marge(world_t * world);

/**
 *\brief La fonction indique si les 2 sprites passés en paramètres sont en collision
 *\param sp1 le premier sprite
 *\param sp2 le deuxième sprite
 */
 int sprites_collide(sprite_t * sp1, sprite_t *sp2);


/**
  *\brief La fonction annule la vitesse de déplacement verticale vy si les 2 sprites passés en paramètres sont en collision
  *\param sp1 le premier sprite
  *\param sp2 le deuxième sprite
  *\param world le monde
*/
 void handle_sprites_collision(sprite_t* sp1, sprite_t* sp2, world_t* world);

 /**
 * \brief La fonction met à jour les données du mur en tenant compte de la physique du monde
 * \param world les données du monde
 */
void update_walls(world_t * world);

/**
 *\brief Gère le tir du missile et sa collision avec les murs.
 *\param world le monde
*/
void tir_missile(world_t *world);

/**
 * \brief La fonction met à jour les données en tenant compte de la physique du monde
 * \param world les données du monde
 */
void update_data(world_t * world);

/**
 * \brief La fonction gère les évènements ayant eu lieu et qui n'ont pas encore été traités
 * \param event paramètre qui contient les événements
 * \param world les données du monde
 */
void handle_events(SDL_Event * event,world_t * world);


#endif
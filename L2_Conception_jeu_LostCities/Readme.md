Lost Cities - Version console

Lancer le jeu
-------------
Le jeu se lance depuis la classe :
LostCities

Au démarrage, un menu s’affiche dans la console pour choisir le mode de jeu (bot contre bot, humain contre bot ou humain contre humain).
Selon le mode choisi, le programme demande le nom du ou des joueurs humains.

Déroulement d’un tour (joueur humain)
-------------------------------------
À chaque tour, l’état du jeu est affiché :
- expéditions des deux joueurs
- main du joueur courant avec les indices des cartes
- défausses par couleur
- nombre de cartes restantes dans la pioche

1) Jouer ou défausser
Le joueur choisit une carte de sa main :
Indice de carte à jouer/défausser :

Puis il choisit l’action :
Type (0 = JOUER, 1 = DÉFAUSSER)

2) Piocher
Après avoir joué ou défaussé, le joueur choisit où piocher :
Pioche (0 = principale, 1 = défausse)

Si 1 est choisi, il faut préciser la couleur :
Couleur (JAUNE, BLANC, BLEU, VERT, ROUGE)

Fin de partie
-------------
La partie se termine lorsque la pioche principale est vide.
Les scores finaux des deux joueurs sont alors affichés.

Règles principales
------------------
- Les cartes sont posées par couleur.
- Les cartes PARI doivent être jouées avant toute carte chiffrée.
- Les cartes chiffrées doivent être posées dans un ordre strictement croissant.
- Une carte peut toujours être défaussée et on ne peut reprendre immédiatement une carte qu'on a soit meme défaussée.

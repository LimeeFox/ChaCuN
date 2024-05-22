
# 1. Introduction

Le projet de cette année consiste à réaliser une version électronique du jeu de société Chasseurs et cueilleurs, dérivé du célèbre Carcassonne. Notre version, qui diffère légèrement de l'originale, se nomme Chasseurs et cueilleurs au Néolithique, abrégé ChaCuN.
## 1.1. Principe

ChaCuN est conçu pour 2 à 5 joueurs, dont le but est de construire progressivement un paysage préhistorique en plaçant côte à côte des tuiles carrées. Les différentes parties du paysage ainsi construit — forêts, rivières, etc. — peuvent être occupées par des chasseurs, cueilleurs ou pêcheurs, dans le but d'obtenir des points.

Les règles exactes seront progressivement expliquées durant les premières étapes du projet, mais le principe général est le suivant. Une partie débute avec la tuile de départ, visible ci-dessous, qui est placée au centre de la surface de jeu.
board_00-initial.png
Figure 1 : La tuile de départ, constituant le paysage initial

À leur tour, les joueurs tirent une nouvelle tuile au hasard, qu'ils placent sur la surface de jeu — éventuellement après l'avoir tournée — de manière à ce qu'elle soit voisine d'au moins une tuile déjà posée, et que les bords des tuiles qui se touchent forment un paysage continu.

Par exemple, si la tuile tirée par le premier joueur est celle visible ci-dessous (à droite), il peut la placer à l'est (droite) de la tuile originale, de manière à ce que la forêt du côté ouest (gauche) de cette tuile continue la forêt du côté est (droite) de la tuile de départ.
board_00-two-tiles.png
Figure 2 : La tuile de départ accompagnée d'une seconde tuile

Cette nouvelle tuile aurait pu être placée de nombreuses autres manières ; par exemple, à l'ouest de la tuile de départ, afin de connecter les deux rivières ; ou encore, au nord de la tuile de départ, après avoir été tournée d'un demi-tour ; et ainsi de suite.

Normalement, chaque joueur ne peut placer qu'une seule tuile durant son tour. Toutefois, si la tuile qu'il pose ferme au moins une forêt contenant un menhir, alors il a le droit de placer une seconde tuile, tirée du tas des « tuiles menhir », distinct du tas normal. Les tuiles menhir sont généralement de plus grande valeur que les autres, et certaines possèdent même un pouvoir spécial, comme nous le verrons ultérieurement.

Après avoir placé une tuile, un joueur peut éventuellement l'occuper au moyen de l'un des 5 pions ou de l'une des 3 huttes qu'il possède. Suivant où ils sont placés, ces pions et huttes jouent différents rôles. Par exemple, un pion placé dans une forêt joue le rôle d'un cueilleur.

Les occupants — pions et huttes — permettent aux joueurs de remporter des points à différents moments de la partie. Par exemple, lorsqu'une forêt est totalement fermée, le ou les joueurs possédant le plus de cueilleurs dans cette forêt remportent un nombre de points qui dépend de la taille de la forêt. Une fois les points comptabilisés, tous les cueilleurs présents dans une forêt fermée sont retournés à leurs propriétaires, qui peuvent les réutiliser ultérieurement pour occuper d'autres tuiles.

En plus des forêts, les autres éléments du paysage qu'il est possible d'occuper sont :

    les rivières, qui peuvent être occupées par des pions jouant le rôle de pêcheurs,
    les prés, qui peuvent être occupés par des pions jouant le rôle de chasseurs,
    les réseaux hydrographiques — constitués de rivières reliées entre elles par des lacs — qui peuvent être occupés par des huttes de pêcheurs.

Les rivières fonctionnent comme les forêts, dans le sens où dès que l'une d'entre elles est terminée aux deux bouts — soit par un lac, soit par un autre élément de paysage — le ou les joueurs y possédant le plus grand nombre de pêcheurs remportent des points, et tous les occupants de la rivière sont retournés à leur propriétaire.

Les prés et les réseaux hydrographiques, par contre, ne rapportent des points à leurs chasseurs et pêcheurs respectifs qu'au moment où la partie se termine, c.-à-d. que la dernière tuile a été posée. Les chasseurs et huttes de pêcheurs posés restent donc à leur place jusqu'à la fin de la partie et ne peuvent pas être réutilisés.
## 1.2. Programme

L'image ci-dessous montre ce à quoi devrait ressembler l'interface graphique de ChaCuN jeu à la fin du projet. On y voit une partie en cours entre cinq joueurs, et les différents éléments de l'interface graphique ont été numérotés de 1 à 5 pour faciliter la description qui suit.
chacun-gui.jpg
Figure 3 : Une partie de ChaCuN (cliquer pour agrandir)

Sur la gauche de l'interface se trouve le plateau de jeu (1) sur lequel les tuiles sont placées. Les cases colorées en violet sont celles sur lesquelles la prochaine tuile pourrait être déposée, et elles sont coloriées de la couleur correspondant à la personne qui doit poser la tuile (Pauline).

Sur la droite de l'interface se trouvent différentes informations. Tout en haut (2), les joueurs, le nombre de points qu'ils ont déjà acquis et les pions et huttes qui leur restent en main sont visibles. Le joueur courant est entouré.

Des messages donnant des informations sur le déroulement de la partie sont affichés au centre (3). Ils permettent par exemple de savoir qu'un joueur a gagné un certain nombre de points à un moment donné, et pourquoi.

Les deux tas de tuiles sont visibles au-dessous (4), celui des tuiles normales à gauche, celui des tuiles menhir à droite. Le nombre affiché sur chacun des tas est celui des cartes restantes.

Finalement, en bas, la tuile à poser est affichée en grand (5).

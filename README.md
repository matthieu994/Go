Ce projet a �t� r�alis� dans le cadre de ma 1ere ann�e de DUT Informatique � l'IUT de S�nart.

LANCEMENT :
Le point d'entr�e du programme se trouve dans le fichier source : src/go/Go.java
Pour lancer le programme vous pouvez:
- Ex�cuter le fichier Go.jar
- Importer le projet dans Eclipse
- � partir de la ligne de commande:
	Compiler:
	> javac components/*.java go/*.java
	Puis ex�cuter:
	> java go/Go

Le dossier go/ contient toutes les classes et fonctions indispensables au fonctionnement du programme.
Le dossier components/ contient les classes h�rit�es des classes 'components' de java, telles que JPanel, JButton, etc.
Le dossier img/ contient toutes les images.
Le dossier saves/ contient les parties sauvegard�es.

FICHIERS SOURCES :
Toutes les classes 'components' h�ritent de composants propres � java.
GameState est une enum�ration pr�sentant les �tats possibles du jeu, tels que START, END, CAPTURE ou TERRITORY.
State est une enum�ration pr�sentant les �tats possibles d'une pierre, tels que BLACK, WHITE ou EMPTY.
Go est le point d'entr�e du programme.
myWindow permet d'ex�cuter les fonctions centrales telles que le lancement d'une partie, le choix de la taille et du temps, l'affichage du gagnant.
Goban contient un panneau GobanGrid, et un panneau Infos, respectivement la grille de jeu, et le panneau des informations; ce fichier source contient toutes les fonctions essentielles � la manipulation des pierres sur le plateau, l'historique de jeu, la sauvegarde dans un fichier, etc.
GobanGrid repr�sente le plateau de jeu et les pierres pr�sentes.
Infos permet d'afficher diverses informations telles que le temps, le score, etc.
Stone est la classe repr�sentant une pierre du plateau, elle comporte des fonctions permettant de calculer ses libert�s, ou � quel territoire elle appartient.

JAVADOC :
http://dwarves.iut-fbleau.fr/~petitm/Javadoc/Go

Ce projet a été réalisé dans le cadre de ma 1ere année de DUT Informatique é l'IUT de Sénart.

# LANCEMENT :
Le point d'entrée du programme se trouve dans le fichier source : src/go/Go.java
Pour lancer le programme vous pouvez:
- Exécuter le fichier Go.jar
- Importer le projet dans Eclipse
- À partir de la ligne de commande:
	Compiler:
	> `javac components/*.java go/*.java`
	Puis exécuter:
	> `java go/Go`

- Le dossier go/ contient toutes les classes et fonctions indispensables au fonctionnement du programme.
- Le dossier components/ contient les classes héritées des classes 'components' de java, telles que JPanel, JButton, etc.
- Le dossier img/ contient toutes les images.
- Le dossier saves/ contient les parties sauvegardées.

# FICHIERS SOURCES :
Toutes les classes 'components' héritent de composants propres é java.
GameState est une enumération présentant les états possibles du jeu, tels que START, END, CAPTURE ou TERRITORY.
State est une enumération présentant les états possibles d'une pierre, tels que BLACK, WHITE ou EMPTY.
Go est le point d'entrée du programme.
myWindow permet d'exécuter les fonctions centrales telles que le lancement d'une partie, le choix de la taille et du temps, l'affichage du gagnant.
Goban contient un panneau GobanGrid, et un panneau Infos, respectivement la grille de jeu, et le panneau des informations; ce fichier source contient toutes les fonctions essentielles é la manipulation des pierres sur le plateau, l'historique de jeu, la sauvegarde dans un fichier, etc.
GobanGrid représente le plateau de jeu et les pierres présentes.
Infos permet d'afficher diverses informations telles que le temps, le score, etc.
Stone est la classe représentant une pierre du plateau, elle comporte des fonctions permettant de calculer ses libertés, ou é quel territoire elle appartient.

## JAVADOC :
http://dwarves.iut-fbleau.fr/~petitm/Javadoc/Go

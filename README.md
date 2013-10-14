interview_test
==============
Ce test consiste à réaliser une petite web app. Les frameworks sont libres, la seul contrainte est que ce soit développé en Java.

Le rôle de l'application est de choisir aléatoirement un restaurant où manger le midi parmi une liste.
   
   
Niveau 1
--------
Web service

    GET /restaurants
Une liste de noms de restaurants est passée en GET.
Cette méthode retourne de manière aléatoire l'un des restaurants de la liste.


Niveau 2
--------
Web service

    POST /restaurants
    {'restaurant': 'resto1'}
Ajoute un restaurant à l'application. Ce restaurant est persisté.

    DELETE /restaurants/resto_name
Supprime le restaurant de l'application et de son support de persistance.

    GET /restaurants
Liste les restaurants insérés dans l'application.

    GET/restaurants/random
Choisi de manière aléatoire, l'un des restaurants de l'application.


- *Les tests sont aussi importants que le code ;*
- *Toute fonctionnalité supplémentaire est la bienvenue.*


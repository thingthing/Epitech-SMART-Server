[![Deployment status from dploy.io](https://smarteip.dploy.io/badge/45290641927501/17321.svg)](http://dploy.io)
# README #

## Configuration et installation du projet ##

### Prérequis ###

* JDK 1.8 : http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* Tomcat 7 : http://tomcat.apache.org/download-80.cgi
* Le projet EIP CS commons disponible sur le bitbucket ou vous avez trouvé ce README

Afin que TomCat et Java fonctionne correctement il est nécessaire de configurer les variables d'environnemt JAVA_HOME et TOMCAT_HOME en indiquant les répertoires d'installation de tomcat et java (le dossier dans lequel vous avez décompressé les zip)

IntelliJ se lance directement depuis le dossier dans lequel il a été décompressé via ./bin/idea.sh

### Import du projet sous intelliJ ###

* File > Project Structure > Project
* Choisir Java 1.8

* Ajouter module (+ vert en haut de la liste des modules) > Import Module > Choisir cs-commons
* Dans l'assistant d'importation > Create module from Existing sources > Next > Cocher le dossier src du module à importer si ce n'est pas déjà fait > Next > Importer le module en lui même et les libs (tout cocher) > Next > Dans review vous devriez avoir à droite le nom du module (cs-commons) et à gauche "libs" > Next > No framework detected > Finish
* Module server > Onglet Dependencies > Ajouter (+ vert à droite) > Module Dependency > cs-commons
* Module server > Onglet Dependencies > Ajouter (+ vert à droite) > Library > cs-commons
* Module server > Onglet Dependencies > Ajouter (+ vert à droite) > Library > libs
* Module server > Onglet Dependencies > Ajouter (+ vert à droite) > Library > Application Server Libraries > Tomcat 8.0 > Add Selected
* Module server > Onglet Sources > Marquer le dossier src comme source

* Dans Project choisir un chemin de sortie (par exemple <repertoire eip>/out

* File > Project Structure > Facets
* Créer une nouvelle Facet de type Web et l'ajouter au module server
* File > Project Structure > Artifacts
* Créer un nouvel Artifact de type Web Application : Exploded à partir du module server
* Artifact > server:war exploded > Onglet Output Layout > Ajouter TOUS les éléments de la colonne de droite dans la colonne de gauche (double clic dessus)


Afin de créer une configuration de lancement
* Run > Edit Configurations
* Créer une nouvelle configuration à l'aide du bouton +.
* Choisir Tomcat Server > Local.
Vous pourrez modifier ici les options de lancement du projet.

Un warning apparait vous informant qu'aucun Artefact n'est configuré.
* Fix et choisir l'artefact server:war exploded créé précédemment..
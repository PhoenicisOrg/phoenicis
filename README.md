# PlayOnLinux and PlayOnMac 5
This is the PlayOnLinux and PlayOnMac 5 repository. 

## Build instructions
### Prerequisites
* maven
* openjdk-8-jdk
* openjfx

#### Ubuntu 14.10+
* sudo apt-get update
* sudo apt-get install maven openjdk-8-jdk openjfx


### Add java-gnome / QTJambi to maven repository
Run the maven goal clean alone at least one to install these jars in your Maven Repository. Otherwise, your build will fail

* mvn clean 


The build instructions can be found on our wiki: http://wiki.playonlinux.com/index.php/Building_PlayOnLinux_5

### Code Quality
To keep code easier to maintain, please import the project specifics format and cleanup settings into your IDE

#### Eclipse
##### CCleanUp
Project > Properties > Java Code Style > Clean Up
* Check Enable project specific settings
* import POL_CleanUp_Settings.xml located in the settings folder

##### Formatter
Project > Properties > Java Code Style > Formatter
* Check Enable project specific settings
* import POL_Formatter_Settings.xml located in the settings folder
	
##### Save Action
You can have eclipse format files for you every time you save.

Window > Preferences > Java > Editor > Save Action
* Check Perform the selected action on save
* Check Format source code
	
## Continous integration
* [Jenkins](http://www.playonlinux.org:8080)
* [SonarQube](http://www.playonlinux.org:9000)
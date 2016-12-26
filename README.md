# PlayOnLinux and PlayOnMac 5
This is the PlayOnLinux and PlayOnMac 5 repository. 

## Scripts
* Please make your pull request on this repository: https://github.com/PlayOnLinux/scripts to add scripts

## Build instructions
[Wiki: Building](https://github.com/PlayOnLinux/POL-POM-5/wiki/Building)

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

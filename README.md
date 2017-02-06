# Phoenicis and PlayOnMac 5
This is the Phoenicis and PlayOnMac 5 repository.

## Scripts
* Please make your pull request on this repository: https://github.com/Phoenicis/scripts to add scripts

## [Build instructions](https://github.com/Phoenicis/POL-POM-5/wiki/Build)

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
* [Jenkins](http://www.phoenicis.org:8080)
* [SonarQube](http://www.phoenicis.org:9000)

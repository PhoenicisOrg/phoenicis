---
title: "Build"
permalink: /build/
toc: true
---

## Dependencies
### Ubuntu 16.04
* Install the dependencies:
```
sudo apt-get update
sudo apt-get install git maven openjdk-8-jdk openjfx libxext6:i386
```

* Set the Java version:
  * OpenJDK
    * `export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"`
  * Oracle JDK
    * `export JAVA_HOME="/usr/lib/jvm/java-8-oracle"`

### Arch Linux
* Install the dependencies. Set the JAVA_HOME after installing `jdk8-openjdk`, but before installing `openjfx`.
  * git
  * jdk8-openjdk
  * java-openjfx
  * maven
  * gradle-1.8 (AUR)

* Set the Java version:
  * OpenJDK
    * `export JAVA_HOME="/usr/lib/jvm/java-8-openjdk"`
  * Oracle JDK
    * `export JAVA_HOME="/usr/lib/jvm/java-8-oracle"`

### Fedora >= 26

* Install the dependencies:
```
sudo dnf install git java-1.8.0-openjdk java-1.8.0-openjdk-openjfx maven openjfx
```

### TrueOS

* Install the dependencies:
```
sudo pkg install git openjdk8 openjfx8-devel maven roboto-fonts-ttf
```

## Build
* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* For TrueOS, create a file called FreeBSD.properties inside phoenicis/phoenicis-configuration/src/main/resources/, similar to Linux.properties, just change those two lines :
```
application.name = Phoenicis PlayOnBSD
#tools.linux-terminal = x-terminal-emulator
```
Note : As there is not FreeBSD wine at the moment, the linux wine package will be displayed in the engine window.

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

## Run
```
cd phoenicis/phoenicis-dist/target
unzip phoenicis-dist.zip -d built
./phoenicis-dist/phoenicis.sh
```

* On TrueOs, do :
```
cd phoenicis/phoenicis-dist/target
unzip phoenicis-dist.zip -d built
bash ./phoenicis-dist/phoenicis.sh
```

## Troubleshooting
#### Old Java version on Arch Linux
Problem:
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/phoenicis/javafx/JavaFXApplication : Unsupported major.minor version 52.0
```
Solution:
Switch to at least Java 8 (see: these [instructions](https://wiki.archlinux.org/index.php/java#Switching_between_JVM))

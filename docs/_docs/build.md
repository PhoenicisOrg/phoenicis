__Content__
* [Dependencies](#dependencies)
     * [Ubuntu](#Ubuntu-1604)
     * [Arch Linux](#arch-linux)
     * [Fedora](#fedora--26)
* [Build](#build)
* [Run](#run)
* [Troubleshooting](#troubleshooting)

# Dependencies
## Ubuntu 16.04
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

## Arch Linux
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

## Fedora >= 26

* Install the dependencies:
```
sudo dnf install git java-1.8.0-openjdk java-1.8.0-openjdk-openjfx maven openjfx
```

# Build
* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/POL-POM-5.git
```

* Build Phoenicis:
```
cd POL-POM-5
mvn clean package
```

# Run
```
cd POL-POM-5/phoenicis-dist/target
unzip phoenicis-dist.zip -d built
./phoenicis-dist/phoenicis.sh
```

# Troubleshooting
### Old Java version on Arch Linux
Problem:
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/phoenicis/javafx/JavaFXApplication : Unsupported major.minor version 52.0
```
Solution:
Switch to at least Java 8 (see: these [instructions](https://wiki.archlinux.org/index.php/java#Switching_between_JVM))

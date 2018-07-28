---
title: "Build"
category: Users
order: 1
toc: true
---

## Ubuntu 18.04 and Linux Mint 19

* Add the Wine repository:
```
sudo apt-add-repository https://dl.winehq.org/wine-builds/ubuntu/
wget -nc https://dl.winehq.org/wine-builds/Release.key && sudo apt-key add Release.key
```

* Remove exising version of Wine:
```
sudo apt-get purge wine* libwine
```

* Install Wine devel packages to fix empty dialog boxes:
```
sudo apt-get update
sudo apt-get install --install-recommends winehq-devel
```

* Install the dependencies:
```
sudo apt-get update
sudo apt-get install git maven openjdk-8-jdk openjfx libxext6:i386
```

* Set the Java version (use Java 8 instead of Java 10):
  * OpenJDK
    * `export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"`
    * `sudo update-java-alternatives --set /usr/lib/jvm/java-1.8.0-openjdk-amd64`
    
* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

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
    
* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

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
    
* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

## Fedora >= 26

* Install the dependencies:
```
sudo dnf install git maven
```

* Install Oracle Java using the steps outlined in the [official Fedora wiki](https://fedoraproject.org/wiki/JDK_on_Fedora#Installing_Oracle_JDK_on_Fedora). Be sure to run the "update-alternatives" commands to change the default Java to Oracle's in case OpenJDK is installed. Due to Fedora's restriction on proprietary software, their OpenJFX library is missing components required for Phoenicis to run.

* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

## TrueOS

* Install the dependencies:
```
sudo pkg install git openjdk8 openjfx8-devel maven roboto-fonts-ttf
```

* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Create a file called `FreeBSD.properties` inside `phoenicis/phoenicis-configuration/src/main/resources/`, similar to `Linux.properties`, just change those two lines :
```
application.name = Phoenicis PlayOnBSD
#tools.linux-terminal = x-terminal-emulator
```
Note : As there is not FreeBSD wine at the moment, the linux wine package will be displayed in the engine window.

## Run

```
cd phoenicis/phoenicis-dist/target
unzip phoenicis-dist.zip -d built
bash ./phoenicis-dist/phoenicis.sh
```

## Troubleshooting

### Maven dependencies cannot be downloaded

* Save an empty JKS file with the default 'changeit' password for Java cacerts.
```
sudo /usr/bin/printf '\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\xe2\x68\x6e\x45\xfb\x43\xdf\xa4\xd9\x92\xdd\x41\xce\xb6\xb2\x1c\x63\x30\xd7\x92' > /etc/ssl/certs/java/cacerts
```

* Re-add all the CA certs into the previously empty file.
```
sudo /var/lib/dpkg/info/ca-certificates-java.postinst configure
```

### Old Java version on Arch Linux

Problem:
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/phoenicis/javafx/JavaFXApplication : Unsupported major.minor version 52.0
```
Solution:
Switch to at least Java 8 (see: these [instructions](https://wiki.archlinux.org/index.php/java#Switching_between_JVM))

---
title: "Build"
category: Users
order: 1
toc: true
---

## Ubuntu 18.04 and Linux Mint 19

* Save an empty JKS file with the default 'changeit' password for Java cacerts.
```
sudo /usr/bin/printf '\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\xe2\x68\x6e\x45\xfb\x43\xdf\xa4\xd9\x92\xdd\x41\xce\xb6\xb2\x1c\x63\x30\xd7\x92' > /etc/ssl/certs/java/cacerts
```

* Re-add all the CA certs into the previously empty file.
```
sudo /var/lib/dpkg/info/ca-certificates-java.postinst configure
```

* Add the Wine repository
```
sudo apt-add-repository https://dl.winehq.org/wine-builds/ubuntu/
wget -nc https://dl.winehq.org/wine-builds/Release.key && sudo apt-key add Release.key
```

* Remove exising version of wine
```
apt-get purge wine* libwine
```

* Update the repository information
```
sudo apt-get update
```

* Install wine packages (This is needed to stop the empty dialog boxes)
```
sudo apt-get install --install-recommends winehq-devel
```
 

* Install packages
```
sudo apt-get -y install git maven openjdk-8-jdk openjfx libxext6:i386
```

* Change to build directory
```mkdir -P ~/build
cd ~/build
```

* Grab the source
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Go in the source directory
```
cd phoenicis
```

* Change the default version of java from 10 to java 8
```
export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
sudo update-java-alternatives --set /usr/lib/jvm/java-1.8.0-openjdk-amd64
```

* Build
```
mvn clean package
```

* Unzip the distribution
```
cd phoenicis-dist/target
unzip phoenicis-dist.zip
```

* Clean up the existing config
```
rm -R ~/.Phoenicis
```

* Run! This is the only line you need from now on.
```
~/build/phoenicis/phoenicis-dist/target/phoenicis-dist/phoenicis.sh
```

There is an issue with the latest steam client not downloading. See https://bugs.winehq.org/show_bug.cgi?id=45329
The if you have this error then shutdown Steam and edit the config.vdf in ~/.Phoenicis/containers/wineprefix/VIRTUAL_DRIVE/drive_c/Program Files/Steam/config

Search for `cip` and the next line down insert:
```
"CS" "valve511.steamcontent.com;valve501.steamcontent.com;valve517.steamcontent.com;valve557.steamcontent.com;valve513.steamcontent.com;valve535.steamcontent.com;valve546.steamcontent.com;valve538.steamcontent.com;valve536.steamcontent.com;valve530.steamcontent.com;valve559.steamcontent.com;valve545.steamcontent.com;valve518.steamcontent.com;valve548.steamcontent.com;valve555.steamcontent.com;valve556.steamcontent.com;valve506.steamcontent.com;valve544.steamcontent.com;valve525.steamcontent.com;valve567.steamcontent.com;valve521.steamcontent.com;valve510.steamcontent.com;valve542.steamcontent.com;valve519.steamcontent.com;valve526.steamcontent.com;valve504.steamcontent.com;valve500.steamcontent.com;valve554.steamcontent.com;valve562.steamcontent.com;valve524.steamcontent.com;valve502.steamcontent.com;valve505.steamcontent.com;valve547.steamcontent.com;valve560.steamcontent.com;valve503.steamcontent.com;valve507.steamcontent.com;valve553.steamcontent.com;valve520.steamcontent.com;valve550.steamcontent.com;valve531.steamcontent.com;valve558.steamcontent.com;valve552.steamcontent.com;valve563.steamcontent.com;valve540.steamcontent.com;valve541.steamcontent.com;valve537.steamcontent.com;valve528.steamcontent.com;valve523.steamcontent.com;valve512.steamcontent.com;valve532.steamcontent.com;valve561.steamcontent.com;valve549.steamcontent.com;valve522.steamcontent.com;valve514.steamcontent.com;valve551.steamcontent.com;valve564.steamcontent.com;valve543.steamcontent.com;valve565.steamcontent.com;valve529.steamcontent.com;valve539.steamcontent.com;valve566.steamcontent.com;valve165.steamcontent.com;valve959.steamcontent.com;valve164.steamcontent.com;valve1611.steamcontent.com;valve1601.steamcontent.com;valve1617.steamcontent.com;valve1603.steamcontent.com;valve1602.steamcontent.com;valve1610.steamcontent.com;valve1615.steamcontent.com;valve909.steamcontent.com;valve900.steamcontent.com;valve905.steamcontent.com;valve954.steamcontent.com;valve955.steamcontent.com;valve1612.steamcontent.com;valve1607.steamcontent.com;valve1608.steamcontent.com;valve1618.steamcontent.com;valve1619.steamcontent.com;valve1606.steamcontent.com;valve1605.steamcontent.com;valve1609.steamcontent.com;valve907.steamcontent.com;valve901.steamcontent.com;valve902.steamcontent.com;valve1604.steamcontent.com;valve908.steamcontent.com;valve950.steamcontent.com;valve957.steamcontent.com;valve903.steamcontent.com;valve1614.steamcontent.com;valve904.steamcontent.com;valve952.steamcontent.com;valve1616.steamcontent.com;valve1613.steamcontent.com;valve958.steamcontent.com;valve956.steamcontent.com;valve906.steamcontent.com"
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

### Old Java version on Arch Linux

Problem:
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/phoenicis/javafx/JavaFXApplication : Unsupported major.minor version 52.0
```
Solution:
Switch to at least Java 8 (see: these [instructions](https://wiki.archlinux.org/index.php/java#Switching_between_JVM))

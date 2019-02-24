---
title: "Build"
category: Developers
order: 1
toc: true
---

## Ubuntu 18.04/18.10
*Updated by [kreyren](https://github.com/Kreyren) at 24.02.2019*

Fork https://github.com/PhoenicisOrg/phoenicis on github before building.

#### Dependencies:
- git
- maven
- java11+
- fakeroot

#### Building:
```bash
# Update repository 
sudo apt update

# Install dependencies
sudo apt install git maven openjdk-11-jdk fakeroot

# Clone your phoenicis repository
git clone https://github.com/<YOUR_GITHUB_FORK_REPO>/phoenicis.git

# Sanity-check your JAVA_HOME variable
echo JAVA_HOME
export JAVA_HOME=PATH # if needed, should be set automatically.

# Build phoenicis
mvn clean package

# Create packages
cd phoenicis-dist/src/scripts
bash phoenicis-create-package.sh
```

#### OPTIONAL: Copypasta method
```bash
echo "INPUT: Enter your github repository URI
eg. https://github.com/Kreyren/phoenicis.git" && read KREYRENNN && sudo apt update && sudo apt install git maven openjdk-11-jdk fakeroot && cd $HOME && git clone $KREYRENNN && cd phoenicis && mvn clean package && cd phoenicis-dist/src/scripts && bash phoenicis-create-package.sh
```

---

## Gentoo Linux
*Updated by [kreyren](https://github.com/Kreyren) at 24.02.2019*

Fork https://github.com/PhoenicisOrg/phoenicis on github before building.

#### Dependencies:
- git
- maven
- java11+
- fakeroot

#### Building:
```bash 
# Update repository
emerge --sync

# Install dependencies
emerge git maven openjdk fakeroot

# Clone your phoenicis repository 
git clone git clone https://github.com/<YOUR_GITHUB_FORK_REPO>/phoenicis.git

# Sanity-check your JAVA_HOME variable
echo JAVA_HOME
export JAVA_HOME=PATH # if needed, should be set automatically.

# Build phoenicis
mvn clean package

# Create packages
cd phoenicis-dist/src/scripts
bash phoenicis-create-package.sh
```

#### OPTIONAL: Copypasta method
```bash
echo "INPUT: Enter your github repository URI
eg. https://github.com/Kreyren/phoenicis.git" && read KREYRENNN && emerge --sync && emerge git maven openjdk fakeroot && cd $HOME && git clone $KREYRENNN && cd phoenicis && mvn clean package && cd phoenicis-dist/src/scripts && bash phoenicis-create-package.sh
```

---

## Arch GNU/Linux

* Install the dependencies.
  * git
  * jdk11
  * maven
  * gradle-1.8 (AUR)
    
* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

---

## Fedora >= 29

* Install the dependencies:
```
sudo dnf install git maven java-11-openjdk-devel
```

* Set the Java version
```
export JAVA_HOME="/usr/lib/jvm/java-11-openjdk"
sudo update-alternatives --config java
```

* Clone the repository from GitHub:
```
git clone https://github.com/PhoenicisOrg/phoenicis.git
```

* Build Phoenicis:
```
cd phoenicis
mvn clean package
```

* Create packages 
```
cd phoenicis-dist/src/scripts
bash phoenicis-create-package.sh
```

---

## TrueOS

* Install the dependencies:
```
sudo pkg install git openjdk11 maven roboto-fonts-ttf
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

---

## Linux From Source
*Updated by [kreyren](https://github.com/Kreyren) at 24.02.2019*

*May the process be painless and successful for you, amen*

#### Dependencies:
- git
- maven
- java 11+
- fakeroot

Install dependencies

#### HINT: OpenJDK Compiltion
```bash
## OpenJDK 11 Linux/x86 (https://hg.openjdk.java.net/jdk-updates/jdk11u/) based on https://hg.openjdk.java.net/jdk-updates/jdk11u/file/cd1c042181e9/doc/building.md
# Download source (86.1 MiB)
wget https://hg.openjdk.java.net/jdk-updates/jdk11u/archive/tip.tar.bz2 -O /tmp/OpenJDK_KREY

# Extract OpenJDK 11
tar -xpf /tmp/OpenJDK_KREY/tip.tar.bz2 -C /tmp/OpenJDK_KREY/

# Configure OpenJDK
bash configure

# Compilation
make images

# Verify your newly build java
./build/*/images/jdk/bin/java -version`

# Run basic tests 
make run-test-tier1
```

#### HINT: OpenJDK precompiled
OpenJDK 11.0.1_p13 Linux/x86 (https://github.com/AdoptOpenJDK/openjdk11-binaries) based on https://gitweb.gentoo.org/repo/gentoo.git/tree/dev-java/openjdk-bin/openjdk-bin-11.0.1_p13.ebuild

#### Building:
```bash
# Clone your phoenicis repository 
git clone git clone https://github.com/<YOUR_GITHUB_FORK_REPO>/phoenicis.git

# Sanity-check your JAVA_HOME variable
echo JAVA_HOME
export JAVA_HOME=PATH # if needed, should be set automatically.

# Build phoenicis
mvn clean package

# Create packages
cd phoenicis-dist/src/scripts
bash phoenicis-create-package.sh
```

---

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

If this doesn't help, you can try to delete the `.m2` directory in your home directory. This will force Maven to re-download all dependencies.

### Old Java version

Problem:
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: org/phoenicis/javafx/JavaFXApplication : Unsupported major.minor version
```
Solution:
Switch to at least Java 10 for running locally (see: these [instructions](https://wiki.archlinux.org/index.php/java#Switching_between_JVM))
Switch to at least Java 11 for creating packages

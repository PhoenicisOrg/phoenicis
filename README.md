# Phoenicis PlayOnLinux and PlayOnMac 5
[![Build Status](https://travis-ci.com/PhoenicisOrg/phoenicis.svg?branch=master)](https://travis-ci.com/PhoenicisOrg/phoenicis)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b667020df53c4b80a22d7e5a73f2b1b3)](https://www.codacy.com/app/PhoenicisOrg/phoenicis?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PhoenicisOrg/phoenicis&amp;utm_campaign=Badge_Grade)
[![Crowdin Badge](https://d322cqt584bo4o.cloudfront.net/phoenicis/localized.svg)](https://crowdin.com/project/phoenicis)

Phoenicis is the designated successor of PlayOnLinux and PlayOnMac 4. It allows you to install and use non-native applications on your favorite operating system.

Supported operating systems:
* Linux
* Mac OSX

Supported engines:
* Wine

------------

## Build and Run
Dependencies:
* Java 10+
* Maven

Build
```
mvn clean package
```
Run
```
mvn install
cd phoenicis-javafx/
mvn exec:java
```

For more details (e.g. regarding dependencies) consider the [documentation](https://phoenicisorg.github.io/phoenicis/).

## Scripts
To add or update scripts, please make pull requests to this repository: https://github.com/PhoenicisOrg/scripts

## Translate
Phoenicis is localized using Crowdin: https://crowdin.com/project/phoenicis
If your language is not listed, please create an [issue](https://github.com/PhoenicisOrg/phoenicis/issues).

## Community
* Issues or ideas: https://github.com/PhoenicisOrg/phoenicis/issues
* Forums : http://www.playonlinux.com/en/forums.html
* Slack : https://join.slack.com/phoenicis-org/shared_invite/MTkzMTMwMjM3MjcxLTE0OTY1MTQzNzktY2IzOTE2NmE3NA

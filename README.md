# Phoenicis PlayOnLinux and PlayOnMac 5
![Build Status](https://github.com/PhoenicisOrg/phoenicis/workflows/CI/badge.svg)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b667020df53c4b80a22d7e5a73f2b1b3)](https://www.codacy.com/app/PhoenicisOrg/phoenicis?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PhoenicisOrg/phoenicis&amp;utm_campaign=Badge_Grade)
[![Crowdin Badge](https://d322cqt584bo4o.cloudfront.net/phoenicis/localized.svg)](https://crowdin.com/project/phoenicis)

Phoenicis is the designated successor of [PlayOnLinux and PlayOnMac 4](https://github.com/PlayOnLinux/POL-POM-4). It allows you to install and use non-native applications on your favorite operating system.

Supported operating systems:
* GNU/Linux
* macOS

Supported engines:
* [Wine](https://www.winehq.org/)

<a href='https://flathub.org/apps/details/org.phoenicis.playonlinux'><img width='240' alt='Download on Flathub' src='https://flathub.org/assets/badges/flathub-badge-en.png'/></a>

# What is Phoenicis PlayOnLinux?

Phoenicis PlayOnLinux is a piece of software which allows you to easily install and use numerous games and apps designed to run with Microsoft® Windows®.

Few games are compatible with GNU/Linux at the moment and it certainly is a factor preventing the migration to this system. Phoenicis PlayOnLinux brings a cost-free, accessible and efficient solution to this problem.

# What are Phoenicis PlayOnLinux’s features?

Here is a non-exhaustive list of the interesting points to know:
* You don’t have to own a Microsoft Windows license to use PlayOnLinux.
* Phoenicis PlayOnLinux is based on [Wine](https://www.winehq.org/), and so profits from all its features yet it keeps the user from having to deal with its complexity.
* Phoenicis PlayOnLinux is [free software](https://en.wikipedia.org/wiki/Free_software).
* Phoenicis PlayOnLinux uses Java.

Nevertheless, Phoenicis PlayOnLinux has some bugs, as every piece of software:
* Occasional performance decrease (image may be less fluid and graphics less detailed).
* Not all games are supported. Nevertheless, you can use our manual installation module.

For more information, visit http://www.playonlinux.com

## Build and Run locally
Dependencies:
* Java 11+
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

For more details (e.g. regarding dependencies) consider the [documentation](https://phoenicisorg.github.io/phoenicis/Developers/build/).

## Build and Run in Online IDE

You can get a complete Phoenicis development setup with Gitpod, a free one-click online IDE for GitHub:

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/PhoenicisOrg/phoenicis)

## Scripts
To add or update scripts, please make pull requests to this repository: https://github.com/PhoenicisOrg/scripts

## Translate
Phoenicis is localized using Crowdin: https://crowdin.com/project/phoenicis.

If your language is not listed, please create an [issue](https://github.com/PhoenicisOrg/phoenicis/issues).

## Community
* Issues or ideas: https://github.com/PhoenicisOrg/phoenicis/issues
* Forums : http://www.playonlinux.com/en/forums.html
* Slack : https://join.slack.com/phoenicis-org/shared_invite/MTkzMTMwMjM3MjcxLTE0OTY1MTQzNzktY2IzOTE2NmE3NA

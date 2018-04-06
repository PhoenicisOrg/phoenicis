---
permalink: /
---

**Phoenicis** is a tool to manage non-native (e.g. Windows) applications in Non-Windows environments (e.g. Linux/Mac OS). It is the successor of PlayOnLinux/PlayOnMac 4.

Phoenicis aims at being the one stop shop platform for non-native applications. To achieve this goal, it focuses on two main principles:
* extensibility
* platform independence

Currently, Phoenicis comes in two flavors:
* Phoenicis PlayOnLinux
* Phoenicis PlayOnMac
It is planned to eventually support Android in the future as well.

First, let's look at some terminology:
* **App** any non-native application which shall be managed by Phoenicis
* **Engine** program which allows you to run an app (e.g. Wine)
* **Container** directory where an app is installed (e.g. a Wine prefix)
* **Shortcut** file describing the startup command for an app
* **Library** collection of the shortcuts of your installed apps
* **Repository** place where the available apps and engines are defined

Knowing PlayOnLinux 4, you might wonder why I said that Phoenicis is first and foremost a _platform_ (and not e.g. a GUI). The heart of Phoenicis is a Java interface to the apps and engines which are written in JavaScript. Based on this, anyone can provide custom repositories and user interfaces. Want to provide a restricted set of Windows applications to a client using MAC OSX? Just collect the apps on a website or even on a DVD and you're ready to go!

Of course Phoenicis also provides some UIs out of the box:
* [Phoenicis CLI](https://github.com/PlayOnLinux/POL-POM-5/wiki/Run#CLI)
* [Phoenicis JavaFX](https://github.com/PlayOnLinux/POL-POM-5/wiki/Run#JavaFX)

And the Phoenicis repository [here](https://github.com/PlayOnLinux/Scripts). 


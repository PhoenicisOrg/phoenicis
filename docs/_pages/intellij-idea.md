---
title: "IntelliJ IDEA"
permalink: /intellij-idea/
toc: true
---

### Import
File → Open → Select folder where you cloned to

### Build
Show Maven view: View → Tool Windows → Maven Projects
Build phoenicis (root) → Lifecycle → package

### Run
Run → Edit Configurations
add: JavaFXApplication with main class org.phoenicis.javafx.JavaFXApplication

### Code Style
File → Settings → Editor → Code Style → Scheme: Manage... → Import → Eclipse XML Profile: select [settings/POL_Formatter_Settings.xml](https://github.com/PlayOnLinux/POL-POM-5/blob/master/settings/POL_Formatter_Settings.xml)

---
title: "Configuration"
permalink: /configuration/
toc: true
---

Phoenicis can be configured via properties. 

You find the default properties in `phoenicis-configuration/src/main/resources/OS.properties`. 

User specific properties in `application.user.settings` override the default properties.<br>
The user specific properties are by default:
- Linux: `~/.Phoenicis/config.properties`
- Mac OS X: `~/Library/Phoenicis/config.properties`

### PoL installation directory
__application.user.root__
Default for Linux:
```
${user.home}/.Phoenicis
```
Default for Mac OS X:
```
${user.home}/Library/Phoenicis
```

### [Scripts repository]({{ site.baseurl }}{% link _pages/repository.md %})
__application.repository.configuration__

Specifies the path where the [scripts](https://github.com/PhoenicisOrg/Scripts) can be found.
For git:
```
git+https://github.com/PhoenicisOrg/Scripts
```
For local directory:
```
file://${application.user.repository}
```
It is also possible to combine multiple repositories:
```
git+https://github.com/PlayOnLiPoL can be configured via properties. 

You find the default properties in `phoenicis-configuration/src/main/resources/OS.properties`. 

User specific properties in `application.user.settings` override the default properties.<br>
The user specific properties are by default:
- Linux: `~/.PlayOnLinux-5/config.properties`
- Mac OS X: `~/Library/PlayOnMac-5/config.properties`

### PoL installation directory
__application.user.root__
Default for Linux:
```
${user.home}/.PlayOnLinux-5
```
Default for Mac OS X:
```
${user.home}/Library/PlayOnMac-5
```

### [Scripts repository](https://github.com/PlayOnLinux/POL-POM-5/wiki/Repository)
__application.repository.configuration__

Specifies the path where the [scripts](https://github.com/PlayOnLinux/Scripts) can be found.
For git:
```
git+https://github.com/PlayOnLinux/Scripts
```
For local directory:
```
file://${application.user.repository}
```
It is also possible to combine multiple repositories:
```
git+https://github.com/PhoenicisOrg/Scripts;file://${application.user.repository}
```

### Theme
__application.theme__

Available themes:
- defaultTheme.css
- darkTheme.css
- hidpiTheme.css

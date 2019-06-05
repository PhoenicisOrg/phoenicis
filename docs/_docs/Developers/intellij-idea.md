---
title: "IntelliJ IDEA"
category: Developers
order: 3
toc: true
---

### Import
File → Open → Select folder where you cloned to

### Build
Show Maven view: View → Tool Windows → Maven Projects
Build phoenicis (root) → Lifecycle → package

### Run
Run → Edit Configurations
add: JavaFXApplication with:
* Name: `JavaFXApplication`
* Main class: `org.phoenicis.javafx.JavaFXApplication`
* VM options: 
```
--add-modules=ALL-MODULE-PATH --module-path=phoenicis-dist/target/lib
```
* **With Java 11**, you may need to remove the = in --module-path
```
--add-modules=ALL-MODULE-PATH --module-path phoenicis-dist/target/lib
```
* Program arguments: empty
* Working directory: `/path/to/phoenicis`
* Environment variables: empty
* Redirect input from: unchecked
* Use classpath of module: `phoenicis-dist`
* Include dependencis with "Provided" scope: unchecked
* JRE: `Default`
* Shorten command line: `user-local: none - java [options]`
* Enable capturing form snapshots: unchecked

### Code Style
File → Settings → Editor → Code Style → Scheme: Manage... → Import → Eclipse XML Profile: select [settings/POL_Formatter_Settings.xml](https://github.com/PhoenicisOrg/phoenicis/blob/master/settings/POL_Formatter_Settings.xml)

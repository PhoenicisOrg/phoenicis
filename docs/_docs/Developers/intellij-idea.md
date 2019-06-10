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
--add-modules=jdk.crypto.ec,java.base,javafx.base,javafx.web,javafx.media,javafx.graphics,javafx.controls,java.naming,java.sql,java.scripting,jdk.scripting.nashorn,jdk.internal.vm.ci,jdk.internal.vm.compiler,org.graalvm.truffle,jdk.jsobject,jdk.xml.dom --module-path phoenicis-dist/target/lib -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI --upgrade-module-path=phoenicis-dist/target/lib/compiler.jar
```
* **With Java 11**, you may need to remove the = in --module-path
```
--add-modules=jdk.crypto.ec,java.base,javafx.base,javafx.web,javafx.media,javafx.graphics,javafx.controls,java.naming,java.sql,java.scripting,jdk.scripting.nashorn,jdk.internal.vm.ci,jdk.internal.vm.compiler,org.graalvm.truffle,jdk.jsobject,jdk.xml.dom --module-path phoenicis-dist/target/lib
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

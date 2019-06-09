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
* main class: org.phoenicis.javafx.JavaFXApplication
* VM options: 
```
--add-modules=jdk.crypto.ec,java.base,javafx.base,javafx.web,javafx.media,javafx.graphics,javafx.controls,java.naming,java.sql,java.scripting,jdk.scripting.nashorn,jdk.internal.vm.ci,jdk.internal.vm.compiler,org.graalvm.truffle,jdk.jsobject,jdk.xml.dom --module-path=phoenicis-dist/target/lib -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -classpath phoenicis-dist/target/lib --upgrade-module-path=phoenicis-dist/target/lib/compiler.jar
```
* **With Java 11**, you may need to remove the = in --module-path
```
--add-modules=jdk.crypto.ec,java.base,javafx.base,javafx.web,javafx.media,javafx.graphics,javafx.controls,java.naming,java.sql,java.scripting,jdk.scripting.nashorn,jdk.internal.vm.ci,jdk.internal.vm.compiler,org.graalvm.truffle,jdk.jsobject,jdk.xml.dom --module-path phoenicis-dist/target/lib
```
![IntelliJ IDEA configuration](/images/intellij-idea-run.png)

### Code Style
File → Settings → Editor → Code Style → Scheme: Manage... → Import → Eclipse XML Profile: select [settings/POL_Formatter_Settings.xml](https://github.com/PhoenicisOrg/phoenicis/blob/master/settings/POL_Formatter_Settings.xml)

---
title: "Interaction between Phoenicis and its Scripts"
category: Developers
order: 10
toc: true
---

The interaction between Phoenicis and its scripts occurs through interfaces.

### Accessing a JavaScript Script from Java
Every script type that Phoenicis uses has its own Java interface.
To access a JavaScript script, Phoenicis proceeds like followed:

1) Execute the script using a `PhoenicisScriptEngine`
2) Cast the result object of the executed script into the desired interface type
3) Interact with the interface object

### Existing interfaces
* [`Engine`](https://github.com/PhoenicisOrg/phoenicis/blob/master/phoenicis-engines/src/main/java/org/phoenicis/engines/Engine.java)
* [`Verb`](https://github.com/PhoenicisOrg/phoenicis/blob/master/phoenicis-engines/src/main/java/org/phoenicis/engines/Verb.java)
* [`Installer`](https://github.com/PhoenicisOrg/phoenicis/blob/master/phoenicis-scripts/src/main/java/org/phoenicis/scripts/Installer.java)
* [`EngineTool`](https://github.com/PhoenicisOrg/phoenicis/blob/master/phoenicis-engines/src/main/java/org/phoenicis/engines/EngineTool.java)
 
### Easy casting via `PhoenicisScriptEngine`
To provide an easy method to perform all three previously mentioned step in a generic way, they could be implemented inside the `PhoenicisScriptEngine` implementations
Example method signatures:

```java
public interface PhoenicisScriptEngine {
    <E> E evalAndReturn(String script, Class<E> type);
    
    <E> void eval(String script, Class<E> type, Consumer<E> onSuccess, Consumer<Exception> onError);
}
```

Alternatively to the `String` script representation it could be a useful idea to pass `ScriptDTO` objects to the `PhoenicisScriptEngine`s.
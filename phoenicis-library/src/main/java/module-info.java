module org.phoenicis.library {
    exports org.phoenicis.library;
    exports org.phoenicis.library.dto;
    requires org.phoenicis.configuration;
    requires spring.beans;
    requires spring.context;
    requires org.phoenicis.scripts;
    requires com.fasterxml.jackson.databind;
    requires commons.lang;
    requires slf4j.api;
    requires org.apache.commons.io;
    requires org.phoenicis.multithreading;
    requires jdk.scripting.nashorn;
}

module org.phoenicis.containers {
    exports org.phoenicis.containers.dto;
    exports org.phoenicis.containers;
    requires org.phoenicis.configuration;
    requires org.phoenicis.multithreading;
    requires org.phoenicis.scripts;
    requires org.phoenicis.win32;
    requires spring.beans;
    requires spring.context;
    requires org.phoenicis.tools;
    requires org.phoenicis.engines;
    requires org.phoenicis.library;
    requires slf4j.api;
    requires spring.core;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires jdk.scripting.nashorn;
    opens org.phoenicis.containers;
}

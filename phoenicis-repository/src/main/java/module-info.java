module org.phoenicis.repository {
    exports org.phoenicis.repository;
    exports org.phoenicis.repository.dto;
    exports org.phoenicis.repository.location;
    exports org.phoenicis.repository.types;
    requires commons.lang;
    requires org.eclipse.jgit;
    requires slf4j.api;
    requires org.phoenicis.tools;
    requires jackson.annotations;
    requires com.fasterxml.jackson.databind;
    requires org.phoenicis.configuration;
    requires org.phoenicis.entities;
    requires spring.core;
    requires org.apache.commons.compress;
    requires spring.beans;
    requires spring.context;
    requires org.phoenicis.multithreading;
    opens org.phoenicis.repository;
}

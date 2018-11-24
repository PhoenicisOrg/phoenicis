module org.phoenicis.javafx {
    requires javafx.base;
    requires javafx.controls;
    requires org.phoenicis.configuration;
    requires com.fasterxml.jackson.databind;
    requires commons.lang;
    requires slf4j.api;
    requires org.phoenicis.scripts;
    requires org.phoenicis.repository;
    requires org.phoenicis.tools;
    requires org.phoenicis.containers;
    requires org.phoenicis.engines;
    requires org.phoenicis.library;
    requires spring.core;
    requires fuzzywuzzy;
    requires java.desktop;
    requires java.sql;
    requires org.phoenicis.multithreading;
    requires spring.context;
    requires org.phoenicis.win32;
    requires org.phoenicis.settings;
    requires javafx.web;
    requires org.phoenicis.entities;
    requires jdk.scripting.nashorn;
    requires spring.beans;
    exports org.phoenicis.javafx;
}

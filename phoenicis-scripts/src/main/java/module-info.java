module org.phoenicis.scripts {
    exports org.phoenicis.scripts;
    exports org.phoenicis.scripts.interpreter;
    exports org.phoenicis.scripts.wizard;
    exports org.phoenicis.scripts.ui;
    requires org.phoenicis.configuration;
    requires spring.context;
    requires spring.beans;
    requires org.phoenicis.multithreading;
    requires org.phoenicis.repository;
    requires org.phoenicis.entities;
    requires org.apache.commons.io;
    requires java.scripting;
    requires jdk.scripting.nashorn;
    opens org.phoenicis.scripts;
    opens org.phoenicis.scripts.wizard;
}

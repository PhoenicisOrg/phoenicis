module phoenicis.configuration {
    exports org.phoenicis.configuration.security;
    exports org.phoenicis.configuration;
    exports org.phoenicis.configuration.localisation;
    requires jackson.annotations;
    requires slf4j.api;
    requires gettext.commons;
    requires spring.beans;
    requires spring.context;
    requires com.fasterxml.jackson.databind;
}
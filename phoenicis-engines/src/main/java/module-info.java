module org.phoenicis.engines {
    exports org.phoenicis.engines;
    exports org.phoenicis.engines.dto;
    requires org.phoenicis.repository;
    requires org.phoenicis.scripts;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.phoenicis.configuration;
    requires slf4j.api;
    requires commons.lang;
    requires jackson.annotations;
    requires org.phoenicis.tools;
    opens org.phoenicis.engines;
}

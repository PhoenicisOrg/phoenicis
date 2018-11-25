module org.phoenicis.engines {
    exports org.phoenicis.engines;
    exports org.phoenicis.engines.dto;
    opens org.phoenicis.engines;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires commons.lang;
    requires jackson.annotations;
    requires org.phoenicis.configuration;
    requires org.phoenicis.repository;
    requires org.phoenicis.scripts;
    requires org.phoenicis.tools;
    requires slf4j.api;
}

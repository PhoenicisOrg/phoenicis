module org.phoenicis.win32 {
    exports org.phoenicis.win32;
    exports org.phoenicis.win32.pe;
    requires spring.context;
    requires commons.lang;
    requires org.apache.commons.io;
    requires org.phoenicis.configuration;
    opens org.phoenicis.win32;
}

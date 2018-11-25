module org.phoenicis.win32 {
    exports org.phoenicis.win32;
    exports org.phoenicis.win32.pe;
    opens org.phoenicis.win32;
    requires commons.lang;
    requires org.apache.commons.io;
    requires org.phoenicis.configuration;
    requires spring.context;
}

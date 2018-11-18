module org.phoenicis.cli {
    exports org.phoenicis.cli.scriptui;
    requires org.phoenicis.configuration;
    requires org.phoenicis.scripts;
    requires org.phoenicis.library;
    requires org.phoenicis.repository;
    requires org.phoenicis.engines;
    requires org.phoenicis.multithreading;
    requires org.phoenicis.tools;
    requires org.phoenicis.win32;
    requires spring.context;
    requires org.jsoup;
    requires commandline;
}

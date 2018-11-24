module org.phoenicis.multithreading {
    exports org.phoenicis.multithreading;
    exports org.phoenicis.multithreading.functional;
    requires commons.lang;
    requires slf4j.api;
    requires spring.context;
    requires java.annotation;
    opens org.phoenicis.multithreading;
}

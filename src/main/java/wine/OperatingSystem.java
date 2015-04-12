package wine;


public enum OperatingSystem {
    MACOSX ("Mac OS X"),
    LINUX ("Linux"),
    FREEBSD ("FreeBSD");

    private String name = "";

    OperatingSystem(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public static OperatingSystem fromString(String name) throws Exception {
        if("Mac OS X".equals(name)) {
            return OperatingSystem.MACOSX;
        }
        if("Linux".equals(name)) {
            return OperatingSystem.LINUX;
        }
        if("FreeBSD".equals(name)) {
            return OperatingSystem.FREEBSD;
        }

        throw new Exception(String.valueOf(System.out.format("Unknown operation system \"%s\"", name)));
    }

    public static String fetchShortName(OperatingSystem operatingSystem) throws Exception {
        switch (operatingSystem) {
            case MACOSX:
                return "MACOSX";
            case LINUX:
                return "LINUX";
            case FREEBSD:
                return "FREEBSD";
            default:
                throw new Exception(String.valueOf(System.out.format("Unknown operation system \"%s\"", operatingSystem.toString())));
        }
    }

    public static OperatingSystem fetchCurrentOperationSystem() throws Exception {
        return OperatingSystem.fromString(System.getProperty("os.name"));
    }


}

package utils;


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

    public static OperatingSystem fromString(String name) throws PlayOnLinuxError {
        if("Mac OS X".equals(name)) {
            return OperatingSystem.MACOSX;
        }
        if("Linux".equals(name)) {
            return OperatingSystem.LINUX;
        }
        if("FreeBSD".equals(name)) {
            return OperatingSystem.FREEBSD;
        }

        throw new PlayOnLinuxError(String.valueOf(System.out.format("Incompatible operation system \"%s\"", name)));
    }

    public String fetchShortName() {
        return this.name();
    }

    public static OperatingSystem fetchCurrentOperationSystem() throws PlayOnLinuxError {
        return OperatingSystem.fromString(System.getProperty("os.name"));
    }


}

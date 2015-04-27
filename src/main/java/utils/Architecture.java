package utils;

public enum Architecture {
    I386,
    AMD64;

    public static Architecture fetchCurrentArchitecture() throws PlayOnLinuxError {
        if(OperatingSystem.fetchCurrentOperationSystem() == OperatingSystem.MACOSX) {
            return I386;
        }
        if("x86".equals(System.getProperty("os.arch"))) {
            return I386;
        } else {
            return AMD64;
        }
    }


    public String getNameForWinePackages() {
        switch (this) {
            case AMD64:
                return "amd64";
            case I386:
            default:
                return "x86";
        }
    }
}

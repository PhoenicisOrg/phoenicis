package scripts;

import utils.OperatingSystem;
import utils.PlayOnLinuxError;

public class EnvironementHelper {
    public static OperatingSystem getOperatinSystem() throws PlayOnLinuxError {
        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public static String getApplicationName() {
        return "PlayOnLinux"; // FIXME
    }
}

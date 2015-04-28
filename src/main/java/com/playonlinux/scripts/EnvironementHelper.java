package com.playonlinux.scripts;

import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.utils.PlayOnLinuxError;

public class EnvironementHelper {
    public static OperatingSystem getOperatinSystem() throws PlayOnLinuxError {
        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public static String getApplicationName() {
        return "PlayOnLinux"; // FIXME
    }
}

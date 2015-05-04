package com.playonlinux.framework;

import com.playonlinux.domain.ScriptClass;
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.domain.PlayOnLinuxError;

@ScriptClass
@SuppressWarnings("unused")
public class EnvironementHelper {
    public static OperatingSystem getOperatinSystem() throws PlayOnLinuxError {
        return OperatingSystem.fetchCurrentOperationSystem();
    }

    public static String getApplicationName() {
        return "PlayOnLinux"; // FIXME
    }
}

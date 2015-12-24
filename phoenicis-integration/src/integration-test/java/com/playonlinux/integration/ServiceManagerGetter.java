package com.playonlinux.integration;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.core.services.manager.ServiceManagerConfiguration;

@Scan
public class ServiceManagerGetter {
    @Inject public static ServiceManager serviceManager;

    public void init(String className) throws ReflectiveOperationException {
        serviceManager.init((ServiceManagerConfiguration) Class.forName(className).newInstance());
    }
}
package com.playonlinux.integration;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.ServiceManager;

@Scan
public class ServiceManagerGetter {
    @Inject public static ServiceManager serviceManager;

}
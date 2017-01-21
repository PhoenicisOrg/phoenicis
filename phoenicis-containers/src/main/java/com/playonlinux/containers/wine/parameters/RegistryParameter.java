package com.playonlinux.containers.wine.parameters;

import com.playonlinux.win32.registry.AbstractRegistryNode;

public interface RegistryParameter {
    AbstractRegistryNode toRegistryPatch();
}

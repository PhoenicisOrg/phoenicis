package com.playonlinux.javafx.views.mainwindow.containers;

import com.playonlinux.containers.dto.ContainerDTO;

import java.lang.reflect.InvocationTargetException;

public class ContainerPanelFactory<T extends AbstractContainerPanel<C>, C extends ContainerDTO> {
    private final Class<T> clazz;
    private final Class<C> entityClazz;

    public ContainerPanelFactory(Class<T> viewClazz, Class<C> entityClazz) {
        this.clazz = viewClazz;
        this.entityClazz = entityClazz;
    }

    public T createContainerPanel(C containerDTO) {
        try {
            return this.clazz.getConstructor(entityClazz).newInstance(containerDTO);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}

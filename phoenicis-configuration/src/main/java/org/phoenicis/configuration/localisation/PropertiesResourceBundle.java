package org.phoenicis.configuration.localisation;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesResourceBundle extends ResourceBundle {
    private Properties properties;

    public PropertiesResourceBundle(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected Object handleGetObject(String key) {
        return this.properties != null ? properties.get(key) : this.parent.getObject(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<String> getKeys() {
        return this.properties != null ? (Enumeration<String>) this.properties.propertyNames() : this.parent.getKeys();
    }

    @Override
    public void setParent(ResourceBundle parent) {
        this.parent = parent;
    }
}
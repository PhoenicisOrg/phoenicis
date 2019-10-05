package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.KeyAttributeListSkin;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KeyAttributeList extends ControlBase<KeyAttributeList, KeyAttributeListSkin> {
    private final ObservableList<KeyAttributePair> keyAttributes;

    private final BooleanProperty editable;

    private final ObjectProperty<Consumer<Map<String, String>>> onChange;

    public KeyAttributeList() {
        super();

        this.keyAttributes = FXCollections.observableArrayList();
        this.editable = new SimpleBooleanProperty();
        this.onChange = new SimpleObjectProperty<>();
    }

    @Override
    public KeyAttributeListSkin createSkin() {
        return new KeyAttributeListSkin(this);
    }

    public ObservableList<KeyAttributePair> getKeyAttributes() {
        return keyAttributes;
    }

    public void setAttributeMap(Map<String, String> attributeMap) {
        this.keyAttributes.setAll(
                attributeMap.entrySet().stream()
                        .map(entry -> new KeyAttributePair(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()));
    }

    public Map<String, String> getAttributeMap() {
        return this.keyAttributes.stream()
                .collect(Collectors.toMap(
                        KeyAttributePair::getKey,
                        KeyAttributePair::getValue));
    }

    public boolean isEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public Consumer<Map<String, String>> getOnChange() {
        return onChange.get();
    }

    public ObjectProperty<Consumer<Map<String, String>>> onChangeProperty() {
        return onChange;
    }

    public void setOnChange(Consumer<Map<String, String>> onChange) {
        this.onChange.set(onChange);
    }

    public static class KeyAttributePair {
        private String key;

        private String value;

        public KeyAttributePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

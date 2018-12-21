package org.phoenicis.javafx.dialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmDialog extends Alert {
    private final ObjectProperty<Runnable> yesCallback;

    private final ObjectProperty<Runnable> noCallback;

    public ConfirmDialog() {
        super(AlertType.CONFIRMATION);

        this.yesCallback = new SimpleObjectProperty<>();
        this.noCallback = new SimpleObjectProperty<>();
    }

    public void showAndCallback() {
        ButtonType result = showAndWait().orElse(ButtonType.CANCEL);
        if (result == ButtonType.OK) {
            Optional.ofNullable(getYesCallback()).ifPresent(Runnable::run);
        } else {
            Optional.ofNullable(getNoCallback()).ifPresent(Runnable::run);
        }
    }

    public Runnable getYesCallback() {
        return yesCallback.get();
    }

    public ObjectProperty<Runnable> yesCallbackProperty() {
        return yesCallback;
    }

    public void setYesCallback(Runnable yesCallback) {
        this.yesCallback.set(yesCallback);
    }

    public Runnable getNoCallback() {
        return noCallback.get();
    }

    public ObjectProperty<Runnable> noCallbackProperty() {
        return noCallback;
    }

    public void setNoCallback(Runnable noCallback) {
        this.noCallback.set(noCallback);
    }
}

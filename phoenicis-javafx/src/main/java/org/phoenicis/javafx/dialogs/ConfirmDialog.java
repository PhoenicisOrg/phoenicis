package org.phoenicis.javafx.dialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * An abstract class for confirm dialogs with two callbacks
 */
public abstract class ConfirmDialog extends Alert {
    /**
     * Callback for {@link ButtonType#OK} button events
     */
    private final ObjectProperty<Runnable> yesCallback;

    /**
     * Callback for other button events than {@link ButtonType#OK}
     */
    private final ObjectProperty<Runnable> noCallback;

    /**
     * Constructor
     */
    protected ConfirmDialog() {
        super(Alert.AlertType.CONFIRMATION);

        this.yesCallback = new SimpleObjectProperty<>();
        this.noCallback = new SimpleObjectProperty<>();

        getDialogPane().getStyleClass().add("phoenicis-dialog");
    }

    /**
     * Displays the {@link SimpleConfirmDialog} and waits for a result.
     * After receiving a result from the dialog call either the yes or no callback
     */
    public void showAndCallback() {
        ButtonType result = showAndWait().orElse(ButtonType.CANCEL);
        if (result == ButtonType.OK) {
            Optional.ofNullable(getYesCallback()).ifPresent(Runnable::run);
        } else {
            Optional.ofNullable(getNoCallback()).ifPresent(Runnable::run);
        }
    }

    public Runnable getYesCallback() {
        return this.yesCallback.get();
    }

    public ObjectProperty<Runnable> yesCallbackProperty() {
        return this.yesCallback;
    }

    public void setYesCallback(Runnable yesCallback) {
        this.yesCallback.set(yesCallback);
    }

    public Runnable getNoCallback() {
        return this.noCallback.get();
    }

    public ObjectProperty<Runnable> noCallbackProperty() {
        return this.noCallback;
    }

    public void setNoCallback(Runnable noCallback) {
        this.noCallback.set(noCallback);
    }
}

package org.phoenicis.javafx.dialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.phoenicis.javafx.dialogs.builder.ConfirmDialogBuilder;

import java.util.Optional;

/**
 * A confirm dialog with two callbacks
 */
public class ConfirmDialog extends Alert {
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
    public ConfirmDialog() {
        super(AlertType.CONFIRMATION);

        this.yesCallback = new SimpleObjectProperty<>();
        this.noCallback = new SimpleObjectProperty<>();
    }

    /**
     * Create a new builder for the confirm dialog
     *
     * @return A new builder instance
     */
    public static ConfirmDialogBuilder builder() {
        return new ConfirmDialogBuilder();
    }

    /**
     * Displays the {@link ConfirmDialog} and waits for a result.
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

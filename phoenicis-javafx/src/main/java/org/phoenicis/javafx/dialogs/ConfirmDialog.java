package org.phoenicis.javafx.dialogs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Window;

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
    private ConfirmDialog() {
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

    /**
     * A builder class for {@link ConfirmDialog} instances
     */
    public static class ConfirmDialogBuilder {
        /**
         * The title of the {@link ConfirmDialog}
         */
        private String title;

        /**
         * The message of the {@link ConfirmDialog}
         */
        private String message;

        /**
         * The success callback of the {@link ConfirmDialog}
         */
        private Runnable yesCallback;

        /**
         * The failure callback of the {@link ConfirmDialog}
         */
        private Runnable noCallback;

        /**
         * The owner window of the {@link ConfirmDialog}
         */
        private Window owner;

        /**
         * The resizable status of the {@link ConfirmDialog}
         */
        private boolean resizable;

        public ConfirmDialogBuilder withTitle(String title) {
            this.title = title;

            return this;
        }

        public ConfirmDialogBuilder withMessage(String message) {
            this.message = message;

            return this;
        }

        public ConfirmDialogBuilder withYesCallback(Runnable yesCallback) {
            this.yesCallback = yesCallback;

            return this;
        }

        public ConfirmDialogBuilder withNoCallback(Runnable noCallback) {
            this.noCallback = noCallback;

            return this;
        }

        public ConfirmDialogBuilder withOwner(Window owner) {
            this.owner = owner;

            return this;
        }

        public ConfirmDialogBuilder withResizable(boolean resizable) {
            this.resizable = resizable;

            return this;
        }

        public ConfirmDialog build() {
            final ConfirmDialog dialog = new ConfirmDialog();

            dialog.initOwner(owner);
            dialog.setTitle(title);
            dialog.setHeaderText(title);
            dialog.setContentText(message);
            dialog.setYesCallback(yesCallback);
            dialog.setNoCallback(noCallback);
            dialog.setResizable(resizable);

            dialog.getDialogPane().getChildren().stream()
                    .filter(node -> node instanceof Label)
                    .map(node -> (Label) node)
                    .forEach(label -> label.setMinHeight(Region.USE_PREF_SIZE));

            return dialog;
        }
    }
}

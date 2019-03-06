package org.phoenicis.javafx.dialogs;

import javafx.scene.layout.Region;
import javafx.stage.Window;

/**
 * A simple confirm dialog with two callbacks
 */
public class SimpleConfirmDialog extends ConfirmDialog {
    /**
     * Constructor
     */
    private SimpleConfirmDialog() {
        super();

        initialise();
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
     * Initializes the components used in the {@link ListConfirmDialog}
     */
    private void initialise() {
        getDialogPane().getStyleClass().add("simple-confirm-dialog");

        getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    }

    /**
     * A builder class for {@link SimpleConfirmDialog} instances
     */
    public static class ConfirmDialogBuilder {
        /**
         * The title of the {@link SimpleConfirmDialog}
         */
        private String title;

        /**
         * The message of the {@link SimpleConfirmDialog}
         */
        private String message;

        /**
         * The success callback of the {@link SimpleConfirmDialog}
         */
        private Runnable yesCallback;

        /**
         * The failure callback of the {@link SimpleConfirmDialog}
         */
        private Runnable noCallback;

        /**
         * The owner window of the {@link SimpleConfirmDialog}
         */
        private Window owner;

        /**
         * The resizable status of the {@link SimpleConfirmDialog}
         */
        private boolean resizable = true;

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

        public SimpleConfirmDialog build() {
            final SimpleConfirmDialog dialog = new SimpleConfirmDialog();

            dialog.initOwner(owner);
            dialog.setTitle(title);
            dialog.setHeaderText(title);
            dialog.setContentText(message);
            dialog.setYesCallback(yesCallback);
            dialog.setNoCallback(noCallback);
            dialog.setResizable(resizable);

            return dialog;
        }
    }
}

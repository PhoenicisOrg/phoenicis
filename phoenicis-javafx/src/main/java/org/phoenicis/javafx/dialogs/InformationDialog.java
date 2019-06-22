package org.phoenicis.javafx.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Window;

/**
 * An information dialog
 */
public class InformationDialog extends Alert {

    /**
     * Constructor
     */
    private InformationDialog() {
        super(AlertType.INFORMATION);
    }

    /**
     * Create a new builder for the information dialog
     *
     * @return A new builder instance
     */
    public static InformationDialogBuilder builder() {
        return new InformationDialogBuilder();
    }

    /**
     * A builder class for {@link InformationDialog} instances
     */
    public static class InformationDialogBuilder {
        /**
         * The title of the {@link InformationDialog}
         */
        private String title;

        /**
         * The message of the {@link InformationDialog}
         */
        private String message;

        /**
         * The owner window of the {@link InformationDialog}
         */
        private Window owner;

        /**
         * The resizable status of the {@link InformationDialog}
         */
        private boolean resizable;

        public InformationDialogBuilder withTitle(String title) {
            this.title = title;

            return this;
        }

        public InformationDialogBuilder withMessage(String message) {
            this.message = message;

            return this;
        }

        public InformationDialogBuilder withOwner(Window owner) {
            this.owner = owner;

            return this;
        }

        public InformationDialogBuilder withResizable(boolean resizable) {
            this.resizable = resizable;

            return this;
        }

        public InformationDialog build() {
            final InformationDialog dialog = new InformationDialog();

            dialog.initOwner(owner);
            dialog.setTitle(title);
            dialog.setHeaderText(title);
            dialog.setContentText(message);
            dialog.setResizable(resizable);

            dialog.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            return dialog;
        }
    }
}

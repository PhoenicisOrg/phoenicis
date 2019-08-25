package org.phoenicis.javafx.dialogs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.stage.Window;

import java.util.List;

/**
 * A list confirmation dialog, showing a list with items the user can confirm
 */
public class ListConfirmDialog extends ConfirmDialog {
    /**
     * A list of items to confirm
     */
    private final ObservableList<String> confirmItems;

    /**
     * Constructor
     */
    private ListConfirmDialog() {
        super();

        this.confirmItems = FXCollections.observableArrayList();

        initialise();
    }

    /**
     * Create a new builder for the confirm dialog
     *
     * @return A new builder instance
     */
    public static ListConfirmDialogBuilder builder() {
        return new ListConfirmDialogBuilder();
    }

    /**
     * Initializes the components used in the {@link ListConfirmDialog}
     */
    private void initialise() {
        getDialogPane().getStyleClass().add("list-confirm-dialog");

        final ListView<String> confirmList = new ListView<>(getConfirmItems());
        confirmList.getStyleClass().add("confirm-list");
        confirmList.setEditable(false);

        getDialogPane().setExpandableContent(confirmList);

        // ensure that the dialog resizes correctly when the expanded state changes
        getDialogPane().expandedProperty().addListener(observable -> Platform.runLater(() -> {
            getDialogPane().requestLayout();

            final Window window = getDialogPane().getScene().getWindow();
            window.sizeToScene();
        }));

        // open the confirm list by default
        getDialogPane().setExpanded(true);

        getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    }

    public ObservableList<String> getConfirmItems() {
        return this.confirmItems;
    }

    /**
     * A builder class for {@link ListConfirmDialog} instances
     */
    public static class ListConfirmDialogBuilder {
        /**
         * The title of the {@link ListConfirmDialog}
         */
        private String title;

        /**
         * The message of the {@link ListConfirmDialog}
         */
        private String message;

        /**
         * A list of the confirm items of the {@link ListConfirmDialog}
         */
        private List<String> confirmItems;

        /**
         * The success callback of the {@link ListConfirmDialog}
         */
        private Runnable yesCallback;

        /**
         * The failure callback of the {@link ListConfirmDialog}
         */
        private Runnable noCallback;

        /**
         * The owner window of the {@link ListConfirmDialog}
         */
        private Window owner;

        /**
         * The resizable status of the {@link ListConfirmDialog}
         */
        private boolean resizable = true;

        public ListConfirmDialogBuilder withTitle(String title) {
            this.title = title;

            return this;
        }

        public ListConfirmDialogBuilder withMessage(String message) {
            this.message = message;

            return this;
        }

        public ListConfirmDialogBuilder withConfirmItems(List<String> confirmItems) {
            this.confirmItems = confirmItems;

            return this;
        }

        public ListConfirmDialogBuilder withYesCallback(Runnable yesCallback) {
            this.yesCallback = yesCallback;

            return this;
        }

        public ListConfirmDialogBuilder withNoCallback(Runnable noCallback) {
            this.noCallback = noCallback;

            return this;
        }

        public ListConfirmDialogBuilder withOwner(Window owner) {
            this.owner = owner;

            return this;
        }

        public ListConfirmDialogBuilder withResizable(boolean resizable) {
            this.resizable = resizable;

            return this;
        }

        public ListConfirmDialog build() {
            final ListConfirmDialog dialog = new ListConfirmDialog();

            dialog.initOwner(owner);
            dialog.setTitle(title);
            dialog.setHeaderText(title);
            dialog.setContentText(message);
            dialog.setYesCallback(yesCallback);
            dialog.setNoCallback(noCallback);
            dialog.setResizable(resizable);

            if (confirmItems != null) {
                dialog.getConfirmItems().setAll(confirmItems);
            }

            return dialog;
        }
    }
}

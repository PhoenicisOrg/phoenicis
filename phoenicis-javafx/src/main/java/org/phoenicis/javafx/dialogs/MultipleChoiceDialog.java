package org.phoenicis.javafx.dialogs;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Region;
import javafx.stage.Window;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * a multiple choice dialog where the user can choose one of multiple items
 */
public class MultipleChoiceDialog extends Alert {
    /**
     * The selected item or null if no item has been selected
     */
    private final ObjectProperty<String> selectedItem;

    /**
     * A list of items to confirm
     */
    private final ObservableList<String> choiceItems;

    /**
     * The selection result callback of the {@link MultipleChoiceDialog}
     */
    private final ObjectProperty<Consumer<String>> selectCallback;

    /**
     * The cancel callback of the {@link MultipleChoiceDialog}
     */
    private final ObjectProperty<Runnable> cancelCallback;

    /**
     * Constructor
     */
    private MultipleChoiceDialog() {
        super(Alert.AlertType.CONFIRMATION);

        this.selectedItem = new SimpleObjectProperty<>();
        this.choiceItems = FXCollections.observableArrayList();

        this.selectCallback = new SimpleObjectProperty<>();
        this.cancelCallback = new SimpleObjectProperty<>();

        initialise();
    }

    /**
     * Create a new builder for the multiple choice dialog
     *
     * @return A new builder instance
     */
    public static MultipleChoiceDialogBuilder builder() {
        return new MultipleChoiceDialogBuilder();
    }

    /**
     * Initializes the components used in the {@link MultipleChoiceDialogBuilder}
     */
    private void initialise() {
        getDialogPane().getStyleClass().addAll("phoenicis-dialog", "multiple-choice-dialog");

        final ComboBox<String> selectionComboBox = new ComboBox<>(getChoiceItems());

        selectedItemProperty().bindBidirectional(selectionComboBox.valueProperty());

        getDialogPane().setExpandableContent(selectionComboBox);

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

    /**
     * Displays the {@link MultipleChoiceDialogBuilder} and waits for a result.
     * After receiving a result from the dialog call either the select callback or the cancel callback
     */
    public void showAndCallback() {
        ButtonType result = showAndWait().orElse(ButtonType.CANCEL);
        if (result == ButtonType.OK) {
            Optional.ofNullable(getSelectCallback()).ifPresent(
                    selectCallback -> selectCallback.accept(getSelectedItem()));
        } else {
            Optional.ofNullable(getCancelCallback()).ifPresent(Runnable::run);
        }
    }

    public String getSelectedItem() {
        return selectedItem.get();
    }

    public ObjectProperty<String> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    public ObservableList<String> getChoiceItems() {
        return choiceItems;
    }

    public Consumer<String> getSelectCallback() {
        return selectCallback.get();
    }

    public ObjectProperty<Consumer<String>> selectCallbackProperty() {
        return selectCallback;
    }

    public void setSelectCallback(Consumer<String> selectCallback) {
        this.selectCallback.set(selectCallback);
    }

    public Runnable getCancelCallback() {
        return cancelCallback.get();
    }

    public ObjectProperty<Runnable> cancelCallbackProperty() {
        return cancelCallback;
    }

    public void setCancelCallback(Runnable cancelCallback) {
        this.cancelCallback.set(cancelCallback);
    }

    /**
     * A builder class for {@link MultipleChoiceDialog} instances
     */
    public static class MultipleChoiceDialogBuilder {
        /**
         * The title of the {@link MultipleChoiceDialog}
         */
        private String title;

        /**
         * The message of the {@link MultipleChoiceDialog}
         */
        private String message;

        /**
         * A list of the confirm items of the {@link MultipleChoiceDialog}
         */
        private List<String> choiceItems;

        /**
         * The selection result callback of the {@link MultipleChoiceDialog}
         */
        private Consumer<String> selectCallback;

        /**
         * The cancel callback of the {@link MultipleChoiceDialog}
         */
        private Runnable cancelCallback;

        /**
         * The owner window of the {@link MultipleChoiceDialog}
         */
        private Window owner;

        /**
         * The resizable status of the {@link MultipleChoiceDialog}
         */
        private boolean resizable = true;

        public MultipleChoiceDialogBuilder withTitle(String title) {
            this.title = title;

            return this;
        }

        public MultipleChoiceDialogBuilder withMessage(String message) {
            this.message = message;

            return this;
        }

        public MultipleChoiceDialogBuilder withChoiceItems(List<String> choiceItems) {
            this.choiceItems = choiceItems;

            return this;
        }

        public MultipleChoiceDialogBuilder withSelectCallback(Consumer<String> selectCallback) {
            this.selectCallback = selectCallback;

            return this;
        }

        public MultipleChoiceDialogBuilder withCancelCallback(Runnable cancelCallback) {
            this.cancelCallback = cancelCallback;

            return this;
        }

        public MultipleChoiceDialogBuilder withOwner(Window owner) {
            this.owner = owner;

            return this;
        }

        public MultipleChoiceDialogBuilder withResizable(boolean resizable) {
            this.resizable = resizable;

            return this;
        }

        public MultipleChoiceDialog build() {
            final MultipleChoiceDialog dialog = new MultipleChoiceDialog();

            dialog.initOwner(owner);
            dialog.setTitle(title);
            dialog.setHeaderText(title);
            dialog.setContentText(message);
            dialog.setSelectCallback(selectCallback);
            dialog.setCancelCallback(cancelCallback);
            dialog.setResizable(resizable);

            if (choiceItems != null) {
                dialog.getChoiceItems().setAll(choiceItems);
            }

            return dialog;
        }
    }
}

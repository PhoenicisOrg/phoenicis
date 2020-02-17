package org.phoenicis.javafx.components.common.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.common.control.KeyAttributeList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class KeyAttributeListSkin extends SkinBase<KeyAttributeList, KeyAttributeListSkin> {
    /**
     * Constructor for all SkinBase instances
     *
     * @param control The control belonging to the skin
     */
    public KeyAttributeListSkin(KeyAttributeList control) {
        super(control);
    }

    @Override
    public void initialise() {
        final TableView<KeyAttributeList.KeyAttributePair> table = createTable();

        final Button addButton = new Button(tr("Add"));
        addButton.setOnAction(event -> {
            getControl().getKeyAttributes().add(new KeyAttributeList.KeyAttributePair("key", "new value"));

            Optional.ofNullable(getControl().getOnChange())
                    .ifPresent(consumer -> consumer.accept(getControl().getAttributeMap()));
        });

        final Button removeButton = new Button(tr("Remove"));
        removeButton.setOnAction(event -> {
            final List<KeyAttributeList.KeyAttributePair> selectedItems = table.getSelectionModel().getSelectedItems();

            getControl().getKeyAttributes().removeAll(selectedItems);

            Optional.ofNullable(getControl().getOnChange())
                    .ifPresent(consumer -> consumer.accept(getControl().getAttributeMap()));
        });

        final HBox buttonContainer = new HBox(addButton, removeButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        getControl().onChangeProperty().addListener(
                (Observable invalidation) -> updateChildren(table, buttonContainer));

        updateChildren(table, buttonContainer);
    }

    private void updateChildren(TableView<KeyAttributeList.KeyAttributePair> table, HBox buttonContainer) {
        if (getControl().isEditable()) {
            final VBox rootContainer = new VBox(table, buttonContainer);

            getChildren().setAll(rootContainer);
        } else {
            getChildren().setAll(table);
        }
    }

    private TableView<KeyAttributeList.KeyAttributePair> createTable() {
        final TableView<KeyAttributeList.KeyAttributePair> table = new TableView<>();

        final TableColumn<KeyAttributeList.KeyAttributePair, String> keyColumn = new TableColumn<>(tr("Key"));
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyColumn.setCellFactory(param -> new KeyAttributeListSkin.EditingCell());
        keyColumn.setOnEditCommit(event -> {
            final ObservableList<KeyAttributeList.KeyAttributePair> items = event.getTableView().getItems();

            final KeyAttributeList.KeyAttributePair entry = items.get(event.getTablePosition().getRow());

            entry.setKey(event.getNewValue());

            Optional.ofNullable(getControl().getOnChange())
                    .ifPresent(consumer -> consumer.accept(getControl().getAttributeMap()));
        });

        final TableColumn<KeyAttributeList.KeyAttributePair, String> attributeColumn = new TableColumn<>(
                tr("Attribute"));
        attributeColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        attributeColumn.setCellFactory(param -> new KeyAttributeListSkin.EditingCell());
        attributeColumn.setOnEditCommit(event -> {
            final ObservableList<KeyAttributeList.KeyAttributePair> items = event.getTableView().getItems();

            final KeyAttributeList.KeyAttributePair entry = items.get(event.getTablePosition().getRow());

            entry.setValue(event.getNewValue());

            Optional.ofNullable(getControl().getOnChange())
                    .ifPresent(consumer -> consumer.accept(getControl().getAttributeMap()));
        });

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().setAll(Arrays.asList(keyColumn, attributeColumn));

        table.editableProperty().bind(getControl().editableProperty());

        Bindings.bindContentBidirectional(table.getItems(), getControl().getKeyAttributes());

        return table;
    }

    /**
     * Taken from: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
     */
    private static class EditingCell extends TableCell<KeyAttributeList.KeyAttributePair, String> {
        private TextField textField;

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();

                createTextField();
                setText(null);
                setGraphic(this.textField);

                this.textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }

                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    commitEdit(textField.getText());
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}

package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author marc
 */
public class DragableRepositoryListCell extends ListCell<RepositoryLocation<? extends Repository>>
        implements ChangeListener<Number> {

    private final BiConsumer<RepositoryLocation<? extends Repository>, Number> onDragDone;

    public DragableRepositoryListCell(BiConsumer<RepositoryLocation<? extends Repository>, Number> onDragDone) {
        super();

        this.onDragDone = onDragDone;
        this.indexProperty().addListener(this);
    }

    @Override
    protected void updateItem(RepositoryLocation<? extends Repository> item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            ObservableList<RepositoryLocation<? extends Repository>> repositories = getListView().getItems();

            GridPane itemPane = new GridPane();

            Label indexLabel = new Label(String.valueOf(repositories.size() - repositories.indexOf(item)));
            indexLabel.setPrefWidth(50);

            Label repositoryLocationLabel = new Label(item.toDisplayString());

            itemPane.add(indexLabel, 0, 0);
            itemPane.add(repositoryLocationLabel, 1, 0);

            setGraphic(itemPane);
        } else {
            setGraphic(null);
        }
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(getIndex()));
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);

            event.consume();
        });

        setOnDragEntered(event -> {
            setOpacity(0.3);
        });

        setOnDragExited(event -> {
            setOpacity(1);
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<RepositoryLocation<? extends Repository>> items = getListView().getItems();
                int draggedIdx = Integer.parseInt(db.getString());
                int thisIdx = items.indexOf(getItem());

                RepositoryLocation<? extends Repository> draggedItem = items.get(draggedIdx);

                items.set(draggedIdx, getItem());
                items.set(thisIdx, draggedItem);

                List<RepositoryLocation<? extends Repository>> itemscopy = new ArrayList<>(getListView().getItems());
                getListView().getItems().setAll(itemscopy);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(event -> {
            onDragDone.accept(getItem(), newValue);

            event.consume();
        });
    }
}

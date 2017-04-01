package org.phoenicis.javafx.views.mainwindow.settings;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

/**
 * @author marc
 *
 */
public class DragableRepositoryListCell extends ListCell<String> implements ChangeListener<Number> {

	private final Runnable onDragDone;
	
	public DragableRepositoryListCell(Runnable onDragDone) {
		super();

		this.onDragDone = onDragDone;
		this.indexProperty().addListener(this);
	}

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		ObservableList<String> repositories = getListView().getItems();

		GridPane itemPane = new GridPane();

		Label indexLabel = new Label(String.valueOf(repositories.size() - repositories.indexOf(item)));
		indexLabel.setPrefWidth(50);

		Label repositoryLocationLabel = new Label(item);

		itemPane.add(indexLabel, 0, 0);
		itemPane.add(repositoryLocationLabel, 1, 0);

		if (!empty) {
			setGraphic(itemPane);
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
			content.putString(getItem());
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
				ObservableList<String> items = getListView().getItems();
				int draggedIdx = items.indexOf(db.getString());
				int thisIdx = items.indexOf(getItem());

				items.set(draggedIdx, getItem());
				items.set(thisIdx, db.getString());

				List<String> itemscopy = new ArrayList<>(getListView().getItems());
				getListView().getItems().setAll(itemscopy);

				success = true;
			}
			event.setDropCompleted(success);

			event.consume();
		});

		setOnDragDone(event -> {
			onDragDone.run();			
			
			event.consume();
		});
	}
}

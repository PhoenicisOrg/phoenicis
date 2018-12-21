package org.phoenicis.javafx.dialogs;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ErrorDialog extends Alert {
    private final ObjectProperty<Exception> exception;

    private ErrorDialog() {
        super(AlertType.ERROR);

        this.exception = new SimpleObjectProperty<>();

        initialise();
    }

    public static ErrorDialogBuilder builder() {
        return new ErrorDialogBuilder();
    }

    private void initialise() {
        contentTextProperty().bind(Bindings.createStringBinding(
                () -> Optional.ofNullable(getException()).map(Exception::getMessage).orElse(null), exception));

        getDialogPane().setExpandableContent(createExpandableContent());

        getDialogPane().expandedProperty().addListener(observable -> Platform.runLater(() -> {
            getDialogPane().requestLayout();

            final Window window = getDialogPane().getScene().getWindow();
            window.sizeToScene();
        }));

        getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    }

    private VBox createExpandableContent() {
        final Label label = new Label(tr("Stack trace:"));

        final TextArea textArea = new TextArea();
        textArea.setEditable(false);

        textArea.textProperty().bind(Bindings.createStringBinding(
                () -> Optional.ofNullable(getException()).map(ExceptionUtils::getFullStackTrace).orElse(null),
                exception));

        VBox.setVgrow(textArea, Priority.ALWAYS);

        final VBox container = new VBox(label, textArea);

        container.setFillWidth(true);

        return container;
    }

    public Exception getException() {
        return exception.get();
    }

    public ObjectProperty<Exception> exceptionProperty() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception.set(exception);
    }

    public static class ErrorDialogBuilder {
        private String message;

        private Exception exception;

        private Window owner;

        private boolean resizable = true;

        public ErrorDialogBuilder withMessage(String message) {
            this.message = message;

            return this;
        }

        public ErrorDialogBuilder withException(Exception exception) {
            this.exception = exception;

            return this;
        }

        public ErrorDialogBuilder withOwner(Window owner) {
            this.owner = owner;

            return this;
        }

        public ErrorDialogBuilder withResizable(boolean resizable) {
            this.resizable = resizable;

            return this;
        }

        public ErrorDialog build() {
            final ErrorDialog dialog = new ErrorDialog();

            dialog.setTitle(tr("Error"));
            dialog.setHeaderText(message);
            dialog.setException(exception);
            dialog.initOwner(owner);
            dialog.setResizable(resizable);

            return dialog;
        }
    }
}

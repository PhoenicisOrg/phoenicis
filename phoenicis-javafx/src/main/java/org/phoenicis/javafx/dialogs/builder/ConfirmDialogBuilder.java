package org.phoenicis.javafx.dialogs.builder;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import org.phoenicis.javafx.dialogs.ConfirmDialog;

public class ConfirmDialogBuilder {
    private String title;

    private String message;

    private Runnable yesCallback;

    private Runnable noCallback;

    private Window owner;

    private boolean resizable;

    private ConfirmDialogBuilder() {
        super();
    }

    public static ConfirmDialogBuilder builder() {
        return new ConfirmDialogBuilder();
    }

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

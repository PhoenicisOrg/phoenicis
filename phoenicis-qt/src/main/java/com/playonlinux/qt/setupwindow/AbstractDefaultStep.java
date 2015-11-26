package com.playonlinux.qt.setupwindow;

import com.playonlinux.core.messages.CancelerMessage;
import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QPushButton;

/**
 * Step used as base for all steps with next/cancel buttons for code-sharing
 * This base provides the default next & cancel - buttons
 */
public abstract class AbstractDefaultStep extends AbstractStep {

    protected QPushButton nextButton;
    protected QPushButton cancelButton;

    public AbstractDefaultStep(CancelerMessage message) {
        super(message);
    }

    @Override
    public void setupButtons(QDialogButtonBox buttonBox) {
        super.setupButtons(buttonBox);

        nextButton = addButton(ButtonType.Next);
        nextButton.clicked.connect(this, "nextButton_clicked()");
        cancelButton = addButton(ButtonType.Cancel);
        cancelButton.clicked.connect(this, "cancelButton_clicked()");
    }


    @SuppressWarnings("unused")
    protected void nextButton_clicked() {
        ((CancelerSynchronousMessage) this.getMessage()).setResponse(null);
    }

    @SuppressWarnings("unused")
    protected void cancelButton_clicked() {
        cancelButton.setEnabled(false);
        this.getMessage().sendCancelSignal();
    }

}

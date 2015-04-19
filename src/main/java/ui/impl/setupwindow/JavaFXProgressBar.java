package ui.impl.setupwindow;

import ui.impl.JavaFXMessageSenderImplementation;
import utils.messages.AsynchroneousMessage;
import utils.messages.Message;

public class JavaFXProgressBar extends javafx.scene.control.ProgressBar implements api.ProgressBar {

    @Override
    public void setProgressPercentage(int value) {
        JavaFXMessageSenderImplementation messageSender = new JavaFXMessageSenderImplementation();
        messageSender.asynchroneousSend(new AsynchroneousMessage() {
                @Override
                public void execute(Message message) {
                    setProgress((double) value / 100.);
                }
            }
        );
    }

    @Override
    public int getProgressPercentage() {
        return (int) (this.getProgress() * 100);
    }
}

package ui.impl.setupwindow.steps;

import javafx.scene.text.Text;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;
import utils.messages.CancelerMessage;
import utils.messages.InterrupterAsynchroneousMessage;

public abstract class AbstractAsynchroneousStepText extends AbstractStepWithHeader {
    String textToShow;

    public AbstractAsynchroneousStepText(JavaFXSetupWindowImplementation parent, CancelerMessage messageWaitingForResponse,
                                         String textToShow) {
        super(parent, messageWaitingForResponse);
        this.textToShow = textToShow;

    }


    @Override
    protected void drawStepContent() {
        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(20);
        textWidget.setWrappingWidth(500);
        textWidget.prefWidth(500);

        this.addToContentPanel(textWidget);
    }


}

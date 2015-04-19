package api;

import utils.messages.CancelerSynchroneousMessage;
import utils.messages.InterrupterAsynchroneousMessage;
import utils.messages.InterrupterSynchroneousMessage;

import java.util.List;

public interface SetupWindow {
    void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow);

    void showYesNoQuestionStep();

    void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue);

    void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems);

    void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow);

    ProgressBar showProgressBar(InterrupterSynchroneousMessage message, String textToShow);

    void close();
}

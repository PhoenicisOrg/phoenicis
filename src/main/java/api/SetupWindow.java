package api;

import utils.CancelerSynchroneousMessage;
import utils.InterrupterAsynchroneousMessage;

import java.util.List;

public interface SetupWindow {
    void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow);

    void showYesNoQuestionStep();

    void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue);

    void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems);

    void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow);

    void close();
}

package api;

import utils.CancelerSynchroneousMessage;
import utils.InterrupterAsynchroneousMessage;

import java.util.List;

public interface SetupWindow {
    void message(CancelerSynchroneousMessage message, String textToShow);

    void question();

    void textbox(CancelerSynchroneousMessage message, String textToShow, String defaultValue);

    void menu(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems);

    void showSpinner(InterrupterAsynchroneousMessage message, String textToShow);

    void close();
}

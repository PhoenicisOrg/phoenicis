package api;

import utils.CancelableMessage;

public interface SetupWindow {
    void message(CancelableMessage message, String textToShow);

    void question();

    void textbox(CancelableMessage message, String textToShow, String defaultValue);
}

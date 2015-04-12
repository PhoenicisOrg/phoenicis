package api;

import utils.Message;

public interface SetupWindow {
    void message(Message message, String textToShow);

    void question();

    void textbox(Message message, String textToShow, String defaultValue);
}

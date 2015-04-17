package api;

import utils.CancelableMessage;

import java.util.List;

public interface SetupWindow {
    void message(CancelableMessage message, String textToShow);

    void question();

    void textbox(CancelableMessage message, String textToShow, String defaultValue);

    void menu(CancelableMessage message, String textToShow, List<String> menuItems);

    void close();
}

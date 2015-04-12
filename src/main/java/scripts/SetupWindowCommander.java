package scripts;

import api.Controller;
import api.SetupWindow;
import api.UIMessageSender;
import utils.Message;

public class SetupWindowCommander {
    static Controller controller;
    private final String title;
    SetupWindow setupWindow;
    UIMessageSender messageSender;

    /**
     * Jython needs static injection, Spring won't work for that purpose
     * @param controller
     */
    public static void injectMainController(Controller controller) {
        SetupWindowCommander.controller = controller;
    }

    public SetupWindowCommander(String title) {
        this.messageSender = controller.createUIMessageSender();
        this.title = title;

        messageSender.synchroneousSend(
                new Message() {
                    @Override
                    public void execute(Message message) {
                        setupWindow = controller.createSetupWindowGUIInstance(title);
                    }
                }
        );
    }


    public void message(String textToShow) throws InterruptedException {
        messageSender.synchroneousSendAndGetResult(
                new Message() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.message(message, textToShow);
                    }
                }
        );
    }

    public String textbox(String textToShow) throws InterruptedException {
        return this.textbox(textToShow, "");
    }

    public String textbox(String textToShow, String defaultValue) throws InterruptedException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new Message<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.textbox(message, textToShow, defaultValue);
                    }

                }
        );
    }
}

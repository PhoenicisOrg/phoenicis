package scripts;

import api.Controller;
import api.SetupWindow;
import api.UIMessageSender;
import utils.AsynchroneousMessage;
import utils.InterrupterAsynchroneousMessage;
import utils.SynchroneousMessage;
import utils.CancelerSynchroneousMessage;

import java.util.List;

public class SetupWizard {
    static Controller controller;
    private final String title;
    SetupWindow setupWindow;
    UIMessageSender messageSender;

    /**
     * Jython needs static injection, Spring won't work for that purpose
     *
     * @param controller controller to be injected
     */
    public static void injectMainController(Controller controller) {
        SetupWizard.controller = controller;
    }

    /**
     * Create the setupWindow
     *
     * @param title title of the setupWindow
     */
    public SetupWizard(String title) {
        this.messageSender = controller.createUIMessageSender();
        this.title = title;

        messageSender.synchroneousSend(
                new SynchroneousMessage() {
                    @Override
                    public void execute(SynchroneousMessage message) {
                        setupWindow = controller.createSetupWindowGUIInstance(title);
                    }
                }
        );
    }

    public void close() {
        messageSender.synchroneousSend(
                new SynchroneousMessage() {
                    @Override
                    public void execute(SynchroneousMessage message) {
                        setupWindow.close();
                    }
                }
        );
    }

    public void message(String textToShow) throws InterruptedException, CancelException {
        messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage() {
                    @Override
                    public void execute(CancelerSynchroneousMessage message) {
                        setupWindow.message(message, textToShow);
                    }
                }
        );
    }

    public String textbox(String textToShow) throws InterruptedException, CancelException {
        return this.textbox(textToShow, "");
    }

    public String textbox(String textToShow, String defaultValue) throws InterruptedException, CancelException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage<String>() {
                    @Override
                    public void execute(CancelerSynchroneousMessage message) {
                        setupWindow.textbox(message, textToShow, defaultValue);
                    }
                }
        );
    }

    public String menu(String textToShow, List<String> menuItems) throws InterruptedException, CancelException {
        return this.menu(textToShow, menuItems, "");
    }

    public String menu(String textToShow, List<String> menuItems, String defaultValue) throws CancelException, InterruptedException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage<String>() {
                    @Override
                    public void execute(CancelerSynchroneousMessage message) {
                        setupWindow.menu(message, textToShow, menuItems);
                    }
                }
        );
    }

    public void wait(String textToShow) {
        messageSender.asynchroneousSend(
                new InterrupterAsynchroneousMessage() {
                    @Override
                    public void execute(InterrupterAsynchroneousMessage message) {
                        setupWindow.showSpinner(message, textToShow);
                    }
                }
        );
    }
}

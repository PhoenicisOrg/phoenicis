package scripts;

import api.Controller;
import api.ProgressStep;
import api.SetupWindow;
import api.UIMessageSender;
import utils.PlayOnLinuxError;
import utils.messages.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static utils.Localisation.Translate;

public class SetupWizard {
    static Controller controller;
    private final String title;

    String MD5_CHECKSUM = "md5";

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
                    public void execute(Message message) {
                        setupWindow = controller.createSetupWindowGUIInstance(title);
                    }
                }
        );
    }

    /**
     * Closes the setupWindow
     */
    public void close() {
        messageSender.synchroneousSend(
                new SynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.close();
                    }
                }
        );
    }

    /**
     * Shows a simple showSimpleMessageStep
     * @param textToShow the text to show
     * @throws InterruptedException
     * @throws CancelException
     */
    public void message(String textToShow) throws InterruptedException, CancelException {
        messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showSimpleMessageStep((CancelerSynchroneousMessage) message, textToShow);
                    }
                }
        );
    }

    /**
     * Ask the user to enter a value
     * @param textToShow a text that will be shown
     * @return the value the user entered
     * @throws InterruptedException
     * @throws CancelException
     */
    public String textbox(String textToShow) throws InterruptedException, CancelException {
        return this.textbox(textToShow, "");
    }

    /**
     * Asks the user to enter a value
     * @param textToShow a text that will be shown
     * @param defaultValue a default value
     * @return the value the user entered
     * @throws InterruptedException
     * @throws CancelException
     */
    public String textbox(String textToShow, String defaultValue) throws InterruptedException, CancelException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showTextBoxStep((CancelerSynchroneousMessage) message, textToShow, defaultValue);
                    }
                }
        );
    }

    /**
     * Displays a showMenuStep so that the user can make a choice
     * @param textToShow a text that will be shown
     * @param menuItems a list containing the elements of the showMenuStep
     * @return the value the user entered (as string)
     * @throws InterruptedException
     * @throws CancelException
     */
    public String menu(String textToShow, List<String> menuItems) throws InterruptedException, CancelException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelerSynchroneousMessage<String>() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showMenuStep((CancelerSynchroneousMessage) message, textToShow, menuItems);
                    }
                }
        );
    }

    /**
     * Displays a showSimpleMessageStep to the user with a waiting symbol, and releases the script just afterward
     * @param textToShow a text that will be shown
     */
    public void wait(String textToShow) {
        messageSender.asynchroneousSend(
                new InterrupterAsynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.showSpinnerStep((InterrupterAsynchroneousMessage) message, textToShow);
                    }
                }
        );
    }

    public ProgressStep progressBar(String textToShow) throws CancelException, InterruptedException {
        return (ProgressStep) messageSender.synchroneousSendAndGetResult(
                new InterrupterSynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        this.setResponse(setupWindow.showProgressBar((InterrupterSynchroneousMessage) message,
                                textToShow));
                    }
                }
        );
    }

    public String download(String remoteUrl) throws IOException, CancelException, InterruptedException {
        Downloader downloader = new Downloader();
        URL remoteFile = new URL(remoteUrl);

        // FIXME: Change APPLICATION_TITLE here
        ProgressStep progressBar = this.progressBar(Translate("Please wait while $APPLICATION_TITLE is downloading:")
                + "\n" +
                downloader.findFileNameFromURL(remoteFile));

        downloader.setProgressBar(progressBar);
        return downloader.Get(remoteFile).getAbsolutePath();
    }

    public void download(String remoteUrl, String localFile) throws IOException, CancelException, InterruptedException {
        Downloader downloader = new Downloader();
        URL remoteFile = new URL(remoteUrl);

        // FIXME: Change APPLICATION_TITLE here
        ProgressStep progressBar = this.progressBar(Translate("Please wait while $APPLICATION_TITLE is downloading:")
                + "\n" +
                downloader.findFileNameFromURL(remoteFile));

        downloader.setProgressBar(progressBar);
        downloader.Get(remoteFile, new File(localFile));
    }

    public String downloadAndCheck(String remoteUrl, String expectedChecksum) throws IOException, CancelException,
            InterruptedException, NoSuchAlgorithmException, PlayOnLinuxError {
        String localFile = this.download(remoteUrl);

        if(!Checksum.calculate(new File(localFile), MD5_CHECKSUM).equals(expectedChecksum)) {
            throw new PlayOnLinuxError("Checksum comparison has failed!");
        }

        return localFile;
    }

    public void downloadAndCheck(String remoteUrl, String localFile, String expectedChecksum) throws IOException,
            CancelException, InterruptedException, NoSuchAlgorithmException, PlayOnLinuxError {
        this.download(remoteUrl, localFile);

        if(!Checksum.calculate(new File(localFile), MD5_CHECKSUM).equals(expectedChecksum)) {
            throw new PlayOnLinuxError("Checksum comparison has failed!");
        }
    }

}

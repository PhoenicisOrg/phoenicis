package scripts;

import api.ProgressStep;

import java.io.*;

import static utils.Localisation.translate;

/* A builder pattern could be used here but we chose not to use it to facilitate scripts syntax
 */

// TODO: Create an abstract class for FileManagement and Downloader
public class FileManagement {
    private SetupWizard setupWizard;
    private ProgressStep progressStep;

    private static final int BLOCK_SIZE = 1024;

    /**
     * Create a downloader object that is not hook into any progress bar
     */
    public FileManagement() {

    }
    public FileManagement(SetupWizard setupWizard) {
        this.setupWizard = setupWizard;
    }

    public FileManagement(ProgressStep progressStep) {
        this.progressStep = progressStep;
    }

    private void defineProgressStep(File sourceFile) throws CancelException, InterruptedException {
        if(this.progressStep == null) {
            // FIXME: Change APPLICATION_TITLE here
            this.progressStep = this.setupWizard.progressBar(
                    translate("Please wait while $APPLICATION_TITLE is copying:") + "\n" +
                            sourceFile.getName()
            );
        }
    }

    private void copyFile(File sourceFile, File destinationFile) throws IOException {
        int fileSize = (int) sourceFile.length();
        float totalDataRead = 0;

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile), BLOCK_SIZE);

        byte[] data = new byte[BLOCK_SIZE];
        int i;
        while((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0)
        {
            totalDataRead += i;
            outputStream.write(data, 0, i);
            if(progressStep != null) {
                int percentCopied = (int) ((totalDataRead * 100) / fileSize);
                progressStep.setProgressPercentage(percentCopied);
            }
        }
        inputStream.close();
        outputStream.close();
    }

    public FileManagement copy(String sourceFilePath, String destinationFilePath)
            throws CancelException, InterruptedException, IOException {
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        this.defineProgressStep(sourceFile);

        this.copyFile(sourceFile, destinationFile);
        return this;
    }


}

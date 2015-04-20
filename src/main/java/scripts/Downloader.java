package scripts;

import api.ProgressStep;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
    ProgressStep progressBar;

    private static final int BLOCK_SIZE = 1024;

    private HttpURLConnection openConnection(URL remoteFile) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) remoteFile.openConnection();

        return connection;
    }
    private void saveConnectionToFile(HttpURLConnection connection, File localFile) throws IOException {
        int fileSize = connection.getContentLength();
        float totalDataRead = 0;

        BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile), BLOCK_SIZE);

        byte[] data = new byte[BLOCK_SIZE];
        int i = 0;
        while((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0)
        {
            totalDataRead += i;
            outputStream.write(data, 0, i);
            if(progressBar != null) {
                int percentDownloaded = (int) ((totalDataRead * 100) / fileSize);
                progressBar.setProgressPercentage(percentDownloaded);
            }
        }
        inputStream.close();
        outputStream.close();
    }

    public void Get(URL remoteFile, File localFile) throws IOException {
        HttpURLConnection connection = openConnection(remoteFile);
        this.saveConnectionToFile(connection, localFile);
    }

    public File Get(URL remoteFile) throws IOException {
        HttpURLConnection connection = openConnection(remoteFile);
        File temporaryFile = File.createTempFile(this.findFileNameFromURL(remoteFile), "");

        Get(remoteFile, temporaryFile);
        return temporaryFile;
    }

    public String findFileNameFromURL(URL remoteFile) {
        String[] urlParts = remoteFile.getFile().split("/");

        return urlParts[urlParts.length - 1];
    }

    public void setProgressBar(ProgressStep progressBar) {
        this.progressBar = progressBar;
    }


}

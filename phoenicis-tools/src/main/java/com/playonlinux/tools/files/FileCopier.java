package com.playonlinux.tools.files;

import com.phoenicis.entities.ProgressEntity;

import java.io.*;
import java.util.function.Consumer;

public class FileCopier {
    private static final int BLOCK_SIZE = 1024;

    public void copyFile(File sourceFile, File destinationFile, Consumer<ProgressEntity> onChange) throws IOException, InterruptedException {
        int fileSize = (int) sourceFile.length();
        float totalDataRead = 0.0F;

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile),
                     BLOCK_SIZE)) {
            byte[] data = new byte[BLOCK_SIZE];
            int i;
            while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
                totalDataRead += i;
                outputStream.write(data, 0, i);
                int percentCopied = (int) (totalDataRead * 100 / fileSize);

                onChange.accept(new ProgressEntity.Builder().withPercent(percentCopied).build());

                if (Thread.interrupted()) {
                    throw new InterruptedException("The copy process was interrupted");
                }
            }
            inputStream.close();
            outputStream.close();
        }
    }

}

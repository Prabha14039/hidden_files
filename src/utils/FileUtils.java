package utils;

import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;

public class FileUtils {

    public static long getClusterSize(String path) throws IOException {
        Path p = FileSystems.getDefault().getPath(path);
        FileStore store = java.nio.file.Files.getFileStore(p);
        long clusterSize = store.getBlockSize();
        System.out.println("Cluster size: " + clusterSize + " bytes");
        return clusterSize;
    }

    public static long readFile(String filePath) {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(filePath, "r");
            long length = file.length();
            return length;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void generationAndSequencing(String filePath, long clusterSize, int parts, int remSize) throws IOException {
        byte[] buffer = new byte[(int) clusterSize];
        new File("Files").mkdirs();

        String fileExtension = "";
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            fileExtension = filePath.substring(dotIndex);
        }

        for (int i = 1; i <=parts; i++) {
            String outputFile = String.format("./Files/files %04d" + fileExtension, 2*i-1);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 1;
                }
                outputStream.write(buffer);
            }

            String OutputFile = String.format("./Files/files %04d" + fileExtension, 2*i);
            try (FileOutputStream OutputStream = new FileOutputStream(OutputFile)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 0;
                }
                OutputStream.write(buffer);
            }
        }

        if (remSize > 0) {
            int startIndex = 2 * parts + 1;

            String remFile = String.format("./Files/files %04d" + fileExtension, startIndex);
            try (FileOutputStream rem = new FileOutputStream(remFile)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 1;
                }
                rem.write(buffer);
            }

            String remFile2 = String.format("./Files/files %04d" + fileExtension, startIndex + 1);
            try (FileOutputStream rem2 = new FileOutputStream(remFile2)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 0;
                }
                rem2.write(buffer);
            }

            String remFile3 = String.format("./Files/files %04d" + fileExtension, startIndex + 2);
            try (FileOutputStream rem3 = new FileOutputStream(remFile3)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 1;
                }
                rem3.write(buffer);
            }
        }

        System.out.printf("The dummy files have successfully been generated: \n%d: size -- %d bytes \n1: size -- %d bytes \n", parts, clusterSize, remSize);
        System.out.printf("The additional files have successfully been generated: \n%d: size -- %d bytes \n2: size -- %d bytes \n", parts, clusterSize, remSize);
    }

    public static void fileSplitter(String filePath,long clusterSize, int parts, int remSize) {
        RandomAccessFile file = null;
        try {
            new File("splitted_files").mkdirs();
            file = new RandomAccessFile(filePath, "r");

            String fileExtension = "";
            int dotIndex = filePath.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
                fileExtension = filePath.substring(dotIndex);
            }

            for (int i = 1; i <=parts; i++) {
            String outputFile = String.format("./splitted_files/Files %04d" + fileExtension, 2*i);
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[(int) clusterSize];
                    int readBuffer = file.read(buffer);
                    outputStream.write(buffer, 0, readBuffer);
                }
            }

            if (remSize > 0) {
            String remFile = String.format("./splitted_files/Files %04d" + fileExtension,2*(parts+1));
                try (FileOutputStream rem = new FileOutputStream(remFile)) {
                    byte[] buffer = new byte[remSize];
                    int readBuffer = file.read(buffer);
                    rem.write(buffer, 0, readBuffer);
                }
            }

            System.out.printf("The %s file has successfully been split: \n%d: size -- %d bytes \n1: size -- %d bytes \n", filePath, parts, clusterSize, remSize);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void fileCombine(long clusterSize, String outputFilePath, int parts, int remSize) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFilePath);

            String fileExtension = "";
            int dotIndex = outputFilePath.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < outputFilePath.length() - 1) {
                fileExtension = outputFilePath.substring(dotIndex);
            }

            for (int i = 1; i <= parts; i++) {
                String inputFile = String.format("./splitted_files/Files %04d" + fileExtension, 2 * i);
                try (FileInputStream inputStream = new FileInputStream(inputFile)) {
                    byte[] buffer = new byte[(int) clusterSize];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            if (remSize > 0) {
                String remFile = String.format("./splitted_files/Files %04d" + fileExtension, 2 * (parts + 1));
                try (FileInputStream inputStream = new FileInputStream(remFile)) {
                    byte[] buffer = new byte[remSize];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            System.out.printf("The files have successfully been combined into: %s\n", outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


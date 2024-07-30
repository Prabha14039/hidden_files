import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileCombiner {

    // Define the fixed source directory path
    private static final String SOURCE_DIRECTORY_PATH = "C:/Users/abhin/OneDrive/Desktop/hidden_files/build/FileD"; // Adjust this path as needed

    public static void combineFiles(String outputFilePath, JTextArea outputArea, JProgressBar progressBar) throws IOException {
        File sourceDirectory = new File(SOURCE_DIRECTORY_PATH);
        File[] files = sourceDirectory.listFiles();

        if (files == null || files.length == 0) {
            throw new IOException("No files found in the source directory.");
        }

        // Sort files by name to ensure correct order
        Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalFiles = files.length;
            int processedFiles = 0;

            for (File file : files) {
                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    processedFiles++;
                    // Update outputArea and progressBar
                    outputArea.append("Processed file: " + file.getName() + "\n");
                    progressBar.setValue((int) ((processedFiles / (float) totalFiles) * 100));
                    progressBar.setString("Combining files: " + processedFiles + "/" + totalFiles);
                }
            }
        }

        outputArea.append("Files have been combined successfully into " + outputFilePath + "\n");
        progressBar.setValue(100);
        progressBar.setString("Completed");
    }
}


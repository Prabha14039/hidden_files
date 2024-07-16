import utils.FileUtils;
import javax.swing.JTextArea;
import java.io.IOException;

public class Main {

    public static void processFile(String inputFilePath, JTextArea outputArea) throws IOException {
        String path;
        long clusterSize = 0;

        try {
            // Check if the OS is Windows
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");
            if (isWindows) {
                path = "C:\\";
            } else {
                path = "/";
            }

            clusterSize = FileUtils.getClusterSize(path);
            outputArea.append("Cluster size determined: " + clusterSize + " bytes\n");
        } catch (IOException e) {
            e.printStackTrace();
            outputArea.append("Error determining cluster size.\n");
            return;
        }

        long contentSize = FileUtils.readFile(inputFilePath);

        if (contentSize <= 0) {
            outputArea.append(String.format("ERROR: the file %s could not be read: %s\n", inputFilePath, new IOException().getMessage()));
            return;
        }

        outputArea.append("Content size: " + contentSize + " bytes\n");

        int parts = (int) (contentSize / clusterSize);
        int rem = (int) (contentSize % clusterSize);

        FileUtils.fileSplitter(inputFilePath, clusterSize, parts, rem);

        try {
            FileUtils.generationAndSequencing(inputFilePath, clusterSize, parts, rem);
            outputArea.append("File processing completed successfully.\n");
        } catch (IOException e) {
            e.printStackTrace();
            outputArea.append("Error during file processing.\n");
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Provide the necessary argument");
            System.exit(1);
        }

        String inputFilePath = args[0];
        JTextArea dummyOutputArea = new JTextArea();
        try {
            processFile(inputFilePath, dummyOutputArea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


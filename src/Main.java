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

    public static void combineFiles(String outputFilePath, JTextArea outputArea) {
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
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int parts = 10;
        int remSize = 100;

        try {
            FileUtils.fileCombine(clusterSize,outputFilePath, parts, remSize);
            outputArea.append("Files have been combined successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
            outputArea.append("Error during file combining.\n");
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Provide the necessary arguments: <action> <file path>");
            System.exit(1);
        }

        String action = args[0];
        String filePath = args[1];
        JTextArea dummyOutputArea = new JTextArea();

        switch (action.toLowerCase()) {
            case "split":
                try {
                    processFile(filePath, dummyOutputArea);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "combine":
                combineFiles(filePath, dummyOutputArea);
                break;
            default:
                System.err.println("Unknown action. Use 'split' or 'combine'.");
                System.exit(1);
        }
    }
}


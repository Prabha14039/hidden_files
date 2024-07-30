import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.JProgressBar;
import java.io.IOException;

public class Main {

    public static void processFile(String inputFilePath, JTextArea outputArea, JProgressBar progressBar) throws IOException {
        String path, drivePath;
        long clusterSize = 0;

        try {
            // Check if the OS is Windows
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");
            if (isWindows) {
                path = "C:\\";
            } else {
                path = "/";
            }

            clusterSize = Encryptor.getClusterSize(path);
            outputArea.append("Cluster size determined: " + clusterSize + " bytes\n");
            drivePath = Encryptor.drive_path();
            outputArea.append("Drive path determined: " + drivePath + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            outputArea.append("Error determining cluster size.\n");
            return;
        }

        long contentSize = Encryptor.readFile(inputFilePath);
        if (contentSize <= 0) {
            outputArea.append(String.format("ERROR: the file %s could not be read: %s\n", inputFilePath, new IOException().getMessage()));
            return;
        }

        outputArea.append("Content size: " + contentSize + " bytes\n");

        if (drivePath != null) {
            int parts = (int) (contentSize / clusterSize);
            int rem = (int) (contentSize % clusterSize);

            // Update progress bar maximum value
            progressBar.setMaximum(3); // 5 additional steps for splitting, addition, dummy, combining, and reservation
            progressBar.setValue(0);

            // Example usage of Encryptor class methods
            try {
                // FileSplitter example
                outputArea.append("Splitting " + inputFilePath + " into " + parts + "\t" + rem + "\n");
                Encryptor.FileSplitter(inputFilePath, clusterSize, parts, rem);
                progressBar.setValue(progressBar.getValue() + 1);
                progressBar.setString("Splitting...");

                // Additionfiles example
                outputArea.append("Creating FileA folder ... \n");
                Encryptor.additionfiles(inputFilePath, clusterSize, parts, rem);
                progressBar.setValue(progressBar.getValue() + 1);
                progressBar.setString("Creating FileA...");

                // CombineFiles example
                outputArea.append("Combining the files and sending them to the target drive: " + drivePath + "\n");
                Encryptor.CombineFiles(drivePath, "./FileA", "./FileD");
                progressBar.setValue(progressBar.getValue() + 1);
                System.out.println("Starting Deletion");
                exFATOperations.deleteEvenNumberedFiles();
                progressBar.setString("Combining...");
                
                // PhysicalClusterReservation example
                // Encryptor.physicalClusterReservation(drivePath);
                // progressBar.setValue(progressBar.getValue() + 1);
                // progressBar.setString("Reserving Clusters...");
                // outputArea.append("Content size: " + contentSize + " bytes\n");

            } catch (IOException e) {
                e.printStackTrace();
                outputArea.append("Error during file processing.\n");
            }
        } else {
            outputArea.append("No removable drive found.\n");
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
        JProgressBar dummyProgressBar = new JProgressBar();

        switch (action.toLowerCase()) {
            case "split":
                try {
                    processFile(filePath, dummyOutputArea, dummyProgressBar);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.err.println("Unknown action. Use 'split' or 'combine'.");
                System.exit(1);
        }
    }
}


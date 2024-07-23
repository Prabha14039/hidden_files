import javax.swing.JTextArea;
import java.io.IOException;

public class Main {

    public static void processFile(String inputFilePath, JTextArea outputArea) throws IOException {
        String path ,drivePath ;
        long clusterSize = 0;

        try {
            // Check if the OS is Windows
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");
            if (isWindows) {
                path = "C:\\";
            } else {
                path = "/";
            }

            clusterSize = Encryptor.getClusterSize(path);// 
            outputArea.append("Cluster size determined: " + clusterSize + " bytes\n");
            drivePath = Encryptor.drive_path();
            outputArea.append("drive path determined: " + drivePath + "\n");
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
            // Example usage of Encryptor class methods
            try {
                // FileSplitter example
                outputArea.append("Splitting " + inputFilePath + "into" + parts + "\t" + rem + "\n");
                Encryptor.FileSplitter(inputFilePath, clusterSize, parts, rem);

                // Additionfiles example
                outputArea.append("Creating FileA folder ... \n");
                Encryptor.additionfiles(inputFilePath, clusterSize, parts, rem);

                // Dummyfiles example
                outputArea.append("Creating FileD folder ... \n");
                Encryptor.dummyfiles(inputFilePath, clusterSize, parts, rem);

                // CombineFiles example
                outputArea.append("Combining the files and sending them to the target drive :" + drivePath + "\n");
                Encryptor.CombineFiles(drivePath, "./FileA", "./FileD");

                // PhysicalClusterReservation example
                Encryptor.physicalClusterReservation(drivePath);
                outputArea.append("Content size: " + contentSize + " bytes\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No removable drive found.");
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

            clusterSize = Encryptor.getClusterSize(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //try {
         //   //Encryptor.fileCombine(clusterSize,outputFilePath, parts, remSize);
          //  outputArea.append("Files have been combined successfully.\n");
       // } catch (Exception e) {
         //   e.printStackTrace();
          //  outputArea.append("Error during file combining.\n");
        //}
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


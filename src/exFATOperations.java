import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class exFATOperations {

    static String drive = "D";
    static int[] FileFormat = {0x46, 0x69, 0x6C, 0x65, 0x30, 0x30, 0x30, 0x31, 0x2E, 0x74, 0x78, 0x74};
    
    public static void main(String[] args) {
        int sectorIndex = 0; 

        byte[] sectorData = readSector.readSector(drive, sectorIndex);

        if (sectorData != null) {
            masterBootSector(sectorData);
        } else {
            System.out.println("Failed to read sector.");
        }

        
    }

    public static void deleteEvenNumberedFiles() {
        File directory = new File("D:\\");
        System.out.println("Starting file deletion");
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("The specified drive is not valid.");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Could not list files in the specified drive.");
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            System.out.println("Current File: " + fileName);
            if (fileName.length() == 12 && fileName.startsWith("File")) {
                try {
                    char check = fileName.charAt(fileName.length()-5);
                    System.out.println("Check evaluated to: "+check);
                    int number = check-'0';

                    if (number % 2 == 0) {
                        if (file.delete()) {
                            System.out.println("Deleted: " + fileName);
                        } else {
                            System.out.println("Failed to delete: " + fileName);
                        }
                    }
                } catch (NumberFormatException e) {
                    // If the substring is not a valid number, ignore this file
                    System.out.println("Invalid File Format");
                }
            }
        }
    }

    public static int convertHexToDecimal(ArrayList<Byte> byteArray) {
        int result = 0;

        // Process the array in little-endian format
        for (int i = 0; i < byteArray.size(); i++) {
            result |= (byteArray.get(i) & 0xFF) << (8 * i);
        }

        return result;
    }

    public static boolean checkFunc(ArrayList<Integer> actualFileName){

        for (int i = 0; i < 4; i++){
            if (actualFileName.get(i)!= FileFormat[i]){
                return false;
            }
        }
        for (int i = 4; i < 8; i++){
            if (FileFormat[i] < 0x30 || FileFormat[i] > 0x39){
                return false;
            }
        }
        for (int i = 8; i < actualFileName.size();i++){
            if (actualFileName.get(i)!= FileFormat[i]){
                return false;
            }
        }

        return true;

    }

    public static ArrayList<Integer> findFirstFile(int rootDirectoryFirstCluster, int clusterSize){
        int sectorCompleted = 0;
        int totalSectors = clusterSize*4;
        int currIndex = 0;
        byte[] currentSector = readSector.readSector(drive,rootDirectoryFirstCluster);
        ArrayList<Integer> returnList = new ArrayList<>();
        returnList.add(0);
        returnList.add(0);


        while (sectorCompleted < totalSectors){
            int firstByte = currentSector[currIndex] & 0xFF;
            if (firstByte == 0x00){
                System.out.println("Did not find the desired file!");
                break;
            }else if (firstByte == 0xC1){
                ArrayList<Integer> fileName = new ArrayList<>();
                for (int i = 0; i < FileFormat.length; i++){
                    fileName.add(currentSector[currIndex+(2*i)+2] & 0xFF);
                }
                boolean checkBool = checkFunc(fileName);

                if (checkBool){
                    returnList.set(0, rootDirectoryFirstCluster+sectorCompleted);
                    returnList.set(1, currIndex);
                    // System.out.println("Found First File");
                    break;
                }else{

                }
            }
            if (currIndex == 15*32){
                currIndex = 0;
                sectorCompleted+=1;
                currentSector = readSector.readSector(drive, rootDirectoryFirstCluster+sectorCompleted);
            }else{
                currIndex+=32;
            } 
        }
        
        return returnList;
    }

    public static ArrayList<Integer> locateClusters(int sectorNumber, int index){

        ArrayList<Integer> thisFileData = new ArrayList<>();
        byte[] thisSectorData = readSector.readSector(drive, sectorNumber);

        System.out.println("Working with sector: "+sectorNumber);
        System.out.println("At index: "+index);
        while (true){
            if (index >= 512){
                index=index-512;
                sectorNumber++;
                thisSectorData = readSector.readSector(drive, sectorNumber);
            }
            ArrayList<Integer> checkArray = new ArrayList<>();
            System.out.println("Inside index: "+index);

            for (int i = 0; i < FileFormat.length;i++){
                checkArray.add(0xFF&thisSectorData[index+2*(i+1)]);
            }
            // System.out.println("Iteration");
            // System.out.println("CheckFunc: "+checkFunc(checkArray));
            if (((thisSectorData[index]&0xFF) != 0xC1) || !checkFunc(checkArray)){
                System.out.println("breaking");
                break;
            }
            index = index+84;
            ArrayList<Byte> listFileClusterOffset = new ArrayList<>();
            ArrayList<Byte> listFileSize = new ArrayList<>();
            if (index >= 512){
                index=index-512;
                sectorNumber++;
                thisSectorData = readSector.readSector(drive, sectorNumber);
            }

            for (int i = 0; i < 4; i++){    
                listFileClusterOffset.add(thisSectorData[index+i]);
                // System.out.println(thisSectorData[index+i]&0xFF);
            }
            index+=4;
            for (int i = 0; i < 4; i++){
                listFileSize.add(thisSectorData[index+i]);
            }
            index+=104;
            
            int fileClusterOffset = convertHexToDecimal(listFileClusterOffset);
            int fileSize = convertHexToDecimal(listFileSize);
            // System.out.println("FileSize:"+fileSize);

            thisFileData.add(fileClusterOffset);
            thisFileData.add(fileSize);
        }

        return thisFileData;
    }

    public static void readAndWriteFiles(ArrayList<Integer> fileClusters, int clusterHeapOffset, int clusterSize){
        byte[] sectorData = {};
        System.out.println("Called read and write files");
        for (int i = 0; i < fileClusters.size();i+=2){
            int address = clusterHeapOffset+((fileClusters.get(i)-2)*clusterSize);
            int numSectors = fileClusters.get(i+1)/512;
            if (numSectors*512 != fileClusters.get(i+1)){
                numSectors++;
            }
            String filePath = "\\\\.\\" + drive+ ":\\File_"+i+"_data.txt";
            System.out.println("Inside saving for loop");
            System.out.println("File: " + filePath + "; Address: " + address);
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                
                for (int j = 0; j < numSectors;j++){
                    sectorData = readSector.readSector(drive, j+address);
                        for (byte b : sectorData) {
                            // Convert byte to hex and write to file
                            fileWriter.write((char)b);
                        }
                }
                fileWriter.write(System.lineSeparator());

            } catch (IOException e) {
                System.err.println("Error writing hex dump to file: " + e.getMessage());
            }
            
            // System.out.println(address);
        }
        
        // for (byte b : sectorData) {
        //     System.out.printf("%02X ", b);
        // }

    }
    
    public static void combineFiles() {
        File directory = new File("D:\\");
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("The specified path is not a valid directory.");
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.matches("File_\\d+_data\\.txt"));
        if (files == null || files.length == 0) {
            System.out.println("No text files found in the specified directory with the pattern 'File_X_data.txt'.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\criticalFile.txt"))) {
            for (File file : files) {
                System.out.println("Processing file: " + file.getName());
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("All files have been combined into: " + "D:\\criticalFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while combining the files.");
        }
    }
    public static void masterBootSector(byte[] sectorData){

        int sectorLength = sectorData.length;

        //Sanity Check
        int lastByte = sectorData[sectorLength-1] & 0xFF;
        int secondLastByte = sectorData[sectorLength-2] & 0xFF;

        if ((lastByte == 0xAA )&& (secondLastByte == 0x55)){
            System.out.println("Sanity Check passed");
        }else{
            System.err.println("Sanity check failed");
            return;
        }

        //Finding parameters and cluster addresses

        int indexClusterHeapOffset = 88;
        int sizeClusterHeapOffset = 4;
    
        int indexFirstClusterOfRootDirectory = 96;
        int sizeFirstClusterOfRootDirectory = 4;

        int indexClusterSize = 109;
        int sizeClusterSize = 1;

        ArrayList<Byte> clusterHeapOffset = new ArrayList<>();
        ArrayList<Byte> FirstClusterOfRootDirectory = new ArrayList<>();
        ArrayList<Byte> sectorSizeArray = new ArrayList<>();

        for (int i = 0; i < sizeClusterHeapOffset;i++){
            clusterHeapOffset.add(sectorData[i+indexClusterHeapOffset]);
        }

        for (int i = 0; i < sizeFirstClusterOfRootDirectory; i++){
            FirstClusterOfRootDirectory.add(sectorData[i+indexFirstClusterOfRootDirectory]);
        }

        for (int i = 0; i < sizeClusterSize; i++){
            sectorSizeArray.add(sectorData[i+indexClusterSize]);
        }

        int clusterHeapSector = convertHexToDecimal(clusterHeapOffset);
        int rootDirectorySector = convertHexToDecimal(FirstClusterOfRootDirectory);
        int clusterSize = 1<<convertHexToDecimal(sectorSizeArray); //calculates number of sector per cluster

        System.out.println("Cluster Size: "+clusterSize);

        rootDirectorySector = clusterHeapSector+((rootDirectorySector-2)*clusterSize);
        System.out.println("Root Driectory sector: "+rootDirectorySector);

        ArrayList<Integer> firstFile = findFirstFile(rootDirectorySector,clusterSize);

        if (firstFile.get(0) == 0 && firstFile.get(1) == 0){
            System.out.println("Did not find the desired filetype.");
            return;
        }

        int sectorNumber = firstFile.get(0);
        int index = firstFile.get(1);

        ArrayList<Integer> fileClusters = locateClusters(sectorNumber, index);
        for (int i = 0; i < fileClusters.size();i++){
            if (i%2==0){
                System.out.println("FileClusters: "+fileClusters.get(i));
            }else{
                System.out.println("Estimated Size in KBs: "+(fileClusters.get(i)/1024));
            }
            
        }

        readAndWriteFiles(fileClusters, clusterHeapSector, clusterSize);
        combineFiles();


        System.out.println(rootDirectorySector);


    }    
}

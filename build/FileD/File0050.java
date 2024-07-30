     String fileExtension = getFileExtension(inputFile);

            int sectorSize = getSectorSize();
            int sectorsPerCluster = getSectorsPerCluster();

            int numFiles = calculateNumberOfDummyFiles(criticalFileSize, sectorSize, sectorsPerCluster);

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            for (int i = 1; i <= numFiles+1; i+=2) {
                int fileNumber = i;
        
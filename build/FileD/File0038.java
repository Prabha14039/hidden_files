e(100);
        progressBar.setString("Completed");
    }

    {
/*
        public static void DummyFiles(String folderPath, String filePath){
            File inputFile = new File(filePath);
            long criticalFileSize = inputFile.length();
            String fileExtension = getFileExtension(inputFile);

            int sectorSize = getSectorSize();
            int sectorsPerCluster = getSectorsPerCluster();

            int numberOfDummyFiles = calculateNumberOfDummyFiles(criticalFileS
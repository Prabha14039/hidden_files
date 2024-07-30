ize, sectorSize, sectorsPerCluster);

            int dummyFileSize = (int)criticalFileSize/numberOfDummyFiles;
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            generateDummyFiles(dummyFileSize, folderPath, numberOfDummyFiles, fileExtension);


        }
        private static int calculateNumberOfDummyFiles(long criticalFileSize, int sectorSize, int sectorsPerCluster) {
            return (int) Math.ce
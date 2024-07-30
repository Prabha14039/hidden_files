il((double) criticalFileSize / (sectorsPerCluster * sectorSize));
        }

        private static void generateDummyFiles(int dummyFileSize, String folderPath, int numberOfFiles, String fileExtension) {
            for (int i = 2; i <= numberOfFiles; i+=2) {
                int fileNumber = i;
                String fileName = String.format("FILE%04d.%s", fileNumber, fileExtension);

                try {
                    createDummyFile(dummyFileSize,folderPath, fileName);
                  
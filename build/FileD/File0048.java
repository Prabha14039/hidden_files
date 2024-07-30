ector size, you might need to change this based on your environment
            return 512;
        }

        private static int getSectorsPerCluster() {
            // Assuming a common number of sectors per cluster, you might need to change this based on your environment
            return 8;
        }

        public static void AdditionalFiles(String folderPath, String filePath){
            File inputFile = new File(filePath);
            long criticalFileSize = inputFile.length();
       
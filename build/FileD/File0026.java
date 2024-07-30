   return fileExtension;
        }
        return fileExtension;
        
    }

    public static void additionfiles(String filePath, long clusterSize, int parts, int remSize) throws IOException {
        byte[] buffer = new byte[(int) clusterSize];
        new File("FileA").mkdirs();

        String fileExtension = Extension(filePath);

        for (int i = 1; i <=parts; i++) {
            String outputFile = String.format("./FileA/File%04d" + fileExtension, 2*i-1);
            try (FileOutp
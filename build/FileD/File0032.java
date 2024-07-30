tic void CombineFiles(String sourceDirectoryPath, String outputFilePath, JTextArea outputArea, JProgressBar progressBar) throws IOException {
        File sourceDirectory = new File(sourceDirectoryPath);
        File[] files = sourceDirectory.listFiles();

        if (files == null || files.length == 0) {
            throw new IOException("No files found in the source directory.");
        }

        Arrays.sort(files, Comparator.comparing(File::getName));

        try (FileOutputStream fos = new 
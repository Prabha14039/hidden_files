c void CombineFiles(String externalDrivePath, String dummyFilesPath, String additionalFilesPath){
        File dummyFilesFolder = new File(dummyFilesPath);
        File additionalFilesFolder = new File(additionalFilesPath);

        if (!dummyFilesFolder.exists() || !additionalFilesFolder.exists()) {
            System.err.println("Folders not found.");
            return;
        }

        // List files in the dummyFiles and additionalFiles folders
        List<File> dummyFiles = Arrays.asList(d
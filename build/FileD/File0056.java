ummyFilesFolder.listFiles());
        List<File> additionalFiles = Arrays.asList(additionalFilesFolder.listFiles());

        // Sort files to ensure correct order
        dummyFiles.sort(Comparator.comparing(File::getName));
        additionalFiles.sort(Comparator.comparing(File::getName));

        // Combine files in an alternating manner
        int maxFiles = Math.max(dummyFiles.size(), additionalFiles.size());
        int dummyIndex = 0, additionalIndex = 0;

        for (int i = 1; i < max
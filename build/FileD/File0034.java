FileOutputStream(outputFilePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalFiles = files.length;
            int processedFiles = 0;

            for (File file : files) {
                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
    
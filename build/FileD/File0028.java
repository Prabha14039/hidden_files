utStream outputStream = new FileOutputStream(outputFile)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 1;
                }
                outputStream.write(buffer);
            }
        }

        if (remSize > 0) {
            int startIndex = 2 * parts + 1;

            String remFile = String.format("./FileA/File%04d" + fileExtension, startIndex);
            try (FileOutputStream rem = new FileOutputStream(remFile)) {
                for (int
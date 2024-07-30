    fileExtension = filePath.substring(dotIndex);
            }

            for (int i = 1; i <=parts; i++) {
            String outputFile = String.format("./FileD/File%04d" + fileExtension, 2*i);
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[(int) clusterSize];
                    int readBuffer = file.read(buffer);
                    outputStream.write(buffer, 0, readBuffer);
                }
            
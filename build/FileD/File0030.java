 j = 0; j < clusterSize; j++) {
                    buffer[j] = 1;
                }
                rem.write(buffer);
            }

            String remFile3 = String.format("./FileA/File%04d" + fileExtension, startIndex + 2);
            try (FileOutputStream rem3 = new FileOutputStream(remFile3)) {
                for (int j = 0; j < clusterSize; j++) {
                    buffer[j] = 1;
                }
                rem3.write(buffer);
            }
        }
    }

   public sta
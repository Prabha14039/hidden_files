  System.out.println("Generated file: " + fileName);
                } catch (IOException e) {
                    System.err.println("Error creating file " + fileName + ": " + e.getMessage());
                }
            }
        }

        private static void createDummyFile(int dummyFileSize, String folderPath, String fileName) throws IOException {
            File file = new File(folderPath, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                String conte
        String fileName = String.format("FILE%04d.%s", fileNumber, fileExtension);
                File file = new File(folderPath, fileName);

                try (FileWriter writer = new FileWriter(file)) {
                    String content = "a".repeat((int)criticalFileSize/(int)numFiles * 1024);
                    writer.write(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
*/
    }

    public stati
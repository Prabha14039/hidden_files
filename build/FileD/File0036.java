                }
                    processedFiles++;
                    outputArea.append("Processed file: " + file.getName() + "\n");
                    progressBar.setValue((int) ((processedFiles / (float) totalFiles) * 100));
                    progressBar.setString("Combining files: " + processedFiles + "/" + totalFiles);
                }
            }
        }

        outputArea.append("Files have been combined successfully into " + outputFilePath + "\n");
        progressBar.setValu
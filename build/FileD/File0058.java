Files * 2; i++) {
            if (i%2==1 && additionalIndex < additionalFiles.size()) {
                // Write additional file
                writeFileToExternalDrive(additionalFiles.get(additionalIndex++), externalDrivePath);
            } else if (dummyIndex < dummyFiles.size()) {
                // Write dummy file
                writeFileToExternalDrive(dummyFiles.get(dummyIndex++), externalDrivePath);
            }
        }

    }

    private static void writeFileToExternalDrive(File 
    // Check if the file name matches the pattern "File-(file_Number).txt"
                if (fileName.matches("FILE\\d{4}"+"TXT")) {
                    // Extract the file number
                    String fileNumberStr = fileName.substring(5, 8);
                    System.out.println(fileNumberStr);
                    int fileNumber = Integer.parseInt(fileNumberStr);

                    //System.out.printf("Processing file: %s, Starting cluster: %d%n", fileName, startingCluster);

          
ull;

            for (int i = 128; i < rootDir.length; i += 32) {
                // Read a directory entry
                String fileName = new String(rootDir, i, 11).trim();
                int startingCluster = ((rootDir[i + 21] & 0xFF) << 16) |
                      ((rootDir[i + 27] & 0xFF) << 8) |
                      (rootDir[i + 26] & 0xFF);
                //String fileNumberStr = fileName.substring(5, fileName.length() - 5);
                System.out.println(fileName);

            
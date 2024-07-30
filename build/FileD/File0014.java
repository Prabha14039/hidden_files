("Debug output of 'fsutil fsinfo volumeinfo " + drivePath + "':");
            while ((line = reader.readLine()) != null) {
                //              System.out.println(line);  // Print each line of output
                if (line.contains("File System Name")) {
                    fileSystemType = line.split(":")[1].trim();
                }
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileSystemT
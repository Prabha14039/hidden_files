  drivePath = drivePath.endsWith("\\") ? drivePath.substring(0, drivePath.length() - 1) : drivePath;

            // Run the 'fsutil fsinfo volumeinfo' command to get file system details
            Process process = Runtime.getRuntime().exec("fsutil fsinfo volumeinfo " + drivePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Print the output for debugging
            //          System.out.println
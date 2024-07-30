ring description = fileSystemView.getSystemTypeDescription(root);
            System.out.println(description);
            if (description != null && (description.contains("Removable") || description.contains("USB Drive"))) {
                // Use the path of the removable drive
                String drivePath = root.getAbsolutePath();
                //      System.out.println("Path of removable drive: " + drivePath);
                // Check file system type
                String fileSystemType 
= getFileSystemType(drivePath);
                return drivePath; 
                //   System.out.println("File System Type: " + fileSystemType);
            } 
            // System.out.println();
        }
        return null ;
    }

    // Method to get the file system type using fsutil
    private static String getFileSystemType(String drivePath) {
        String fileSystemType = "Unknown";

        try {
            // Ensure the drive path does not have a trailing backslash
          
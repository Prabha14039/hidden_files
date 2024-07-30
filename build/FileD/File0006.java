ystem.out.println("Cluster size: " + clusterSize + " bytes");
        return clusterSize;
    }

    public static String drive_path()
    {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File[] roots = File.listRoots();

        //System.out.println("Drives:");

        for (File root : roots) 
        {
            // Print basic information about each root

            // Check if the drive is removable or has other specific descriptions
            St
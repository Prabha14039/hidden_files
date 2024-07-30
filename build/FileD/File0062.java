printStackTrace();
        }
    }

    public static void physicalClusterReservation(String externalDrivePath){
        externalDrivePath = externalDrivePath.replace("\\","/");
        int rootDirSize = calcRootdir(externalDrivePath);
        ClusterNode head = FAT32ReadandDelete(externalDrivePath);
    }

    private static int calcRootdir(String externalDrivePath) {
        String filePath = externalDrivePath;
        int sectorSize = 512; // Typical sector size for FAT

        try (Random
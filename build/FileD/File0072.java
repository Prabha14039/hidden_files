     return clusterCount * sectorsPerCluster * sectorSize/1024;

            //System.out.printf("Root directory size: %d bytes%n", rootDirSize);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static ClusterNode FAT32ReadandDelete(String externalDrivePath){
        int sectorSize = 512; // Typical sector size for FAT

        try (RandomAccessFile device = new RandomAccessFile(externalDrivePath, "r")) {
            // Read t
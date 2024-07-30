le
            int fatStartSector = reservedSectors;
            int fatSize = sectorsPerFAT * sectorSize;

            // Read the first FAT copy
            device.seek(fatStartSector * sectorSize);
            byte[] fat = new byte[fatSize];
            device.readFully(fat);

            // Traverse the cluster chain for the root directory
            int cluster = rootCluster;
            int clusterCount = 0;

            while (cluster < 0x0FFFFFF8) { // End of cluster chain for FAT32
 
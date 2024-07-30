         int rootDirStartSector = fatStartSector + numberOfFATs * sectorsPerFAT;

            int rootDirSize = calcRootdir(externalDrivePath) * 1024; // 32 KB for the root directory
            //System.out.println(rootDirSize);
            byte[] rootDir = new byte[rootDirSize];
            device.seek(rootDirStartSector * sectorSize);
            device.readFully(rootDir);

            // Process the root directory entries
            ClusterNode head = null;
            ClusterNode current = n
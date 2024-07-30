((bootSector[38] & 0xFF) << 16) |
                                ((bootSector[37] & 0xFF) << 8) |
                                (bootSector[36] & 0xFF);
            int rootCluster = ((bootSector[47] & 0xFF) << 24) |
                              ((bootSector[46] & 0xFF) << 16) |
                              ((bootSector[45] & 0xFF) << 8) |
                              (bootSector[44] & 0xFF);
            int sectorsPerCluster = bootSector[13] & 0xFF;

            // Start of the first FAT tab
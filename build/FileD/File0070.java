               clusterCount++;
                int fatIndex = cluster * 4;
                cluster = ((fat[fatIndex + 3] & 0xFF) << 24) |
                          ((fat[fatIndex + 2] & 0xFF) << 16) |
                          ((fat[fatIndex + 1] & 0xFF) << 8) |
                          (fat[fatIndex] & 0xFF);
                cluster &= 0x0FFFFFFF; // Mask to get only 28 bits as FAT32 uses 28 bits for cluster addresses
            }

            // Calculate the size of the root directory
       
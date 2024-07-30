] & 0xFF) << 8) |
                                (bootSector[36] & 0xFF);

            // Start of the first FAT table
            int fatStartSector = reservedSectors;
            int fatSize = sectorsPerFAT * sectorSize;

            // Read the first FAT copy
            device.seek(fatStartSector * sectorSize);
            byte[] fat = new byte[fatSize];
            device.readFully(fat);

            // Read the root directory (for simplicity, assuming it's located after the FAT area)
   
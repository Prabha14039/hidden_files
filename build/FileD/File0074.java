he boot sector
            byte[] bootSector = new byte[sectorSize];
            device.readFully(bootSector);

            // Extract necessary information from the boot sector
            int reservedSectors = ((bootSector[15] & 0xFF) << 8) | (bootSector[14] & 0xFF);
            int numberOfFATs = bootSector[16] & 0xFF;
            int sectorsPerFAT = ((bootSector[39] & 0xFF) << 24) |
                                ((bootSector[38] & 0xFF) << 16) |
                                ((bootSector[37
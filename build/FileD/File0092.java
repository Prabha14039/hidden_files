                fat[fatIndex + 2] = 0x00;
                fat[fatIndex + 3] = 0x00;
                temp = temp.next;
            }

            // Write the updated FAT table back to the device
            device.seek(fatStartSector * sectorSize);
            device.write(fat);

            System.out.println("Marked clusters as free and updated FAT table.");
            return temp;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
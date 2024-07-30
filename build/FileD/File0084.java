          // Check if the file number is even
                    if (fileNumber % 2 == 0) {
                        // Follow the cluster chain in the FAT table
                        int cluster = startingCluster;

                        while (cluster < 0x0FFFFFF8) { // End of cluster chain for FAT32
                            //System.out.printf("Cluster address: %08X%n", cluster);

                            // Create a new node for the cluster address
                            ClusterNo
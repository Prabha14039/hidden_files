de newNode = new ClusterNode(cluster);

                            if (head == null) {
                                head = newNode;
                                current = head;
                            } else {
                                current.next = newNode;
                                current = newNode;
                            }

                            // Get the next cluster from the FAT table
                            int fatIndex = cluster * 4;
              
}

            System.out.println("Linked list of clusters for files ending with an even number:");
            ClusterNode temp = head;
            while (temp != null) {
                System.out.printf("Cluster address: %08X%n", temp.clusterAddress);
                temp = temp.next;
            }
            temp = head;
            while (temp != null) {
                int fatIndex = temp.clusterAddress * 4;
                fat[fatIndex] = 0x00;
                fat[fatIndex + 1] = 0x00;

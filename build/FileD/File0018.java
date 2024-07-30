    e.printStackTrace();
                }
            }
        }
    }

    public static void FileSplitter(String filePath,long clusterSize, int parts, int remSize)
    {
        RandomAccessFile file = null;
        try {
            new File("FileD").mkdirs();
            file = new RandomAccessFile(filePath, "r");

            String fileExtension = "";
            int dotIndex = filePath.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            
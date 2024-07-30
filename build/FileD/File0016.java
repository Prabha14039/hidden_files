ype;
    }

    public static long readFile(String filePath) {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(filePath, "r");
            long length = file.length();
            return length;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                
  if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String Extension(String filePath)
    {
        String fileExtension = "";
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) 
        {
            fileExtension = filePath.substring(dotIndex);
         
file, String externalDrivePath) {
        try (FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(externalDrivePath + file.getName())) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

        } catch (IOException e) {
            System.err.println("Error writing file: " + file.getName());
            e.
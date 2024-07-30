}

            if (remSize > 0) {
            String remFile = String.format("./FileD/File%04d" + fileExtension,2*(parts+1));
                try (FileOutputStream rem = new FileOutputStream(remFile)) {
                    byte[] buffer = new byte[remSize];
                    int readBuffer = file.read(buffer);
                    rem.write(buffer, 0, readBuffer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
          
nt = "a".repeat(dummyFileSize * 1024);
                writer.write(content);
            }
        }

        private static String getFileExtension(File file) {
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf == -1) {
                return ""; // empty extension
            }
            return name.substring(lastIndexOf + 1);
        }

        private static int getSectorSize() {
            // Assuming a common s
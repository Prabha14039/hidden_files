import java.io.File;

public class main {
    public static void main(String[] args) {
        // List all available file roots (drives)
        File[] roots = File.listRoots();

        for (File root : roots) {
            System.out.println("Drive: " + root.getAbsolutePath());
            System.out.println("Is Drive Writable? " + root.canWrite());
            System.out.println("Total Space: " + root.getTotalSpace());
            System.out.println("Usable Space: " + root.getUsableSpace());
            System.out.println("Free Space: " + root.getFreeSpace());
            System.out.println();
        }
    }
}


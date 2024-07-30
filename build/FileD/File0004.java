FileSystemView;


class ClusterNode {
    int clusterAddress;
    ClusterNode next;

    public ClusterNode(int clusterAddress) {
        this.clusterAddress = clusterAddress;
        this.next = null;
    }
}

public class Encryptor {

    public static long getClusterSize(String path) throws IOException {
        Path p = FileSystems.getDefault().getPath(path);
        FileStore store = java.nio.file.Files.getFileStore(p);
        long clusterSize = store.getBlockSize();
        //    S
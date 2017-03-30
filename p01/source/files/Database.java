package files;

import java.io.*;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {
    private static final int DEFAULT_STORAGE_SPACE = 1024 * 1000; // 1024 kBytes

    private Map<String, String> backedUpFiles; // key = filename, value = fileId
    private int storageSpace = DEFAULT_STORAGE_SPACE;
    private String path = "Database.txt";

    public int getStorageSpace() {
        return storageSpace;
    }

    public Database() {
        this.backedUpFiles = new HashMap<>();
    }

    public Map<String, String> getBackedUpFiles() {
        return backedUpFiles;
    }

    public boolean addFileToDatabase(String filename, String fileId, int size) {

        if (this.storageSpace < size) return false;

        if (!this.backedUpFiles.containsKey(filename)) {
            this.backedUpFiles.put(filename, fileId);
            this.storageSpace -= size;
            return true;
        }
        return false;
    }

    public boolean removeFileFromDatabase(String filename, int size) {
        if (this.backedUpFiles.remove(filename) == null) {
            System.out.println("Could not remove file '" + filename + "' from database, because it didn't exist.");
            return false;
        } else {
            this.storageSpace += size;
            return true;
        }
    }

    public void storeFile() {

        try {

            FileWriter fstream = new FileWriter("Database.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("size: " + storageSpace);

            Set<String> chaves = backedUpFiles.keySet();
            for (String chave : chaves) {
                out.newLine();
                out.write(chave + " = " + backedUpFiles.get(chave));

            }

            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public void loadFile() {

        File f = new File(path);

        if (!f.exists()) {
            System.out.println("Sem database ");
            return;
        }

        try {
            FileInputStream fstream = new FileInputStream("Database.txt");

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            if ((strLine = br.readLine()) != null) {

                String[] result = strLine.split("[a-z, :]+");
                System.out.println("size in file" + result[1]);
                storageSpace = Integer.parseInt(result[1]);
            }

            while ((strLine = br.readLine()) != null) {
                String[] par = strLine.split(" = ");

                this.addFileToDatabase(par[0], par[1], 0);
                System.out.println(par[0] + "----" + par[1]);
            }

            in.close();
        } catch (Exception e) {
            System.err.println(e.getCause());
        }
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        Database data = new Database();
        data.loadFile();
        System.out.println("muda");
        data.storeFile();
        System.out.println(data.getStorageSpace());

    }
}

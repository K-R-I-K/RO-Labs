package client;

import db.entity.File;
import db.entity.Folder;
import util.SerializationUtility;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Client {
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    public Client(String ip, int port) throws IOException {
        sock = new Socket(ip,port);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream( )));
        out = new PrintWriter(sock.getOutputStream(), true);
    }
    private Object sendQuery(int operation, Object ...values) throws IOException {
        StringBuilder query = new StringBuilder(String.valueOf(operation));
        for (Object o: values) {
            query.append("#").append(SerializationUtility.serialize((Serializable) o));
        }
        out.println(query);

        String response = in.readLine();
        String[] fields = response.split("#");
        try {
            int comp_code = Integer.parseInt(fields[0]);
            Object result = SerializationUtility.deSerialize(fields[1]);
            if (comp_code == 0) {
                return result;
            } else {
                throw new IOException("Error while processing query");
            }
        } catch(NumberFormatException e) {
            throw new IOException("Invalid response from server");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public Folder addNewFolder(Folder folder) throws IOException {
        return (Folder) sendQuery(0, folder);
    }
    public boolean deleteFolder(Folder folder) throws IOException {
        return (Boolean) sendQuery(1, folder);
    }
    public File addNewFileToFolder(File file, Folder folder) throws IOException {
        return (File) sendQuery(2, file, folder);
    }
    public boolean deleteFile(File file, Folder folder) throws IOException {
        return (Boolean) sendQuery(3, file, folder);
    }
    public boolean updateFile(File file) throws IOException {
        return (Boolean) sendQuery(4, file);
    }
    public boolean transferFile(File file, Folder folder) throws IOException {
        return (Boolean) sendQuery(5, file, folder);
    }
    public File copyFileToFolder(File file, Folder folder) throws IOException {
        return (File) sendQuery(2, file, folder);
    }
    public List<Folder> getAllFolders() throws IOException {
        return (List<Folder>) sendQuery(6);
    }
    public List<File> getFilesInFolder(Folder folder) throws IOException {
        return (List<File>) sendQuery(7, folder);
    }
    public void disconnect() throws IOException {
        sock.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost",12345);

            System.out.println("\nAdd a new Folder");
            Folder folder = Folder.createFolder("myFolder");
            System.out.println(client.addNewFolder(folder));
            System.out.println(client.getAllFolders());

            System.out.println("\nAdd a new File to Folder");
            File file1 = File.createFile("myFile", 10, 0);
            System.out.println(client.addNewFileToFolder(file1, folder)); //insert file to folder
            System.out.println(client.getFilesInFolder(folder)); //all files in folder

            System.out.println("\nupdate File");
            file1.setSize(999);
            System.out.println(client.updateFile(file1)); //update file
            System.out.println(client.getFilesInFolder(folder)); //all files in folder

            System.out.println("\ntransfer File");
            List<Folder> folders = client.getAllFolders();
            System.out.println(client.transferFile(file1, folders.get(0))); //transfer file to folder
            System.out.println(client.getFilesInFolder(folders.get(0))); //all files in folder
            System.out.println(client.getFilesInFolder(folder)); //all files in folder

            System.out.println("\ncopy File to Folder");
            folders = client.getAllFolders();
            System.out.println(client.copyFileToFolder(file1, folders.get(1))); //copy file to folder
            System.out.println(client.getFilesInFolder(folders.get(0))); //all files in folder
            System.out.println(client.getFilesInFolder(folders.get(1))); //all files in folder

            System.out.println("\ndelete File");
            folders = client.getAllFolders();
            System.out.println(client.deleteFile(file1, folders.get(0))); //delete file
            System.out.println(client.getFilesInFolder(folders.get(0))); //all files in folder

            System.out.println("\ndelete Folder");
            System.out.println(client.deleteFolder(folder)); // delete folder
            System.out.println(client.getAllFolders());

            client.disconnect();
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}

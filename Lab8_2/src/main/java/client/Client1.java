package client;

import server.RemoteController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client1 {
    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry(1234);
        RemoteController server = (RemoteController) registry.lookup("dbController");
        server.addClient();
        System.out.println("RMI object found");
        while (true) {
            if(server.getNumberClients() == 2 ){
                System.out.println(server.getAllFolders());
                Thread.sleep(100);
                System.out.println(server.getAllFolders());
                break;
            }
        }
        /*System.out.println("\nAdd a new Folder");
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
        System.out.println(client.getAllFolders());*/
    }
}

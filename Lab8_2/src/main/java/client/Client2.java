package client;

import db.entity.Folder;
import lombok.SneakyThrows;
import server.RemoteController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client2 {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1234);
        RemoteController server = (RemoteController) registry.lookup("dbController");
        server.addClient();

        System.out.println("RMI object found");
        while (true) {
            if (server.getNumberClients() == 2) {
                Folder folder = Folder.createFolder("myFolder");
                System.out.println(server.addNewFolder(folder));
                break;
            }
        }
    }
}

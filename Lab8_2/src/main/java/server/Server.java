package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) throws RemoteException {
        RemoteController server = new RemoteControllerImpl();
        RemoteController stub = (RemoteController) UnicastRemoteObject.exportObject(server, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind("dbController", stub);
    }
}

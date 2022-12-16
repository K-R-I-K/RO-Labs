package rmi;

import rmi.server.RemoteController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry(1234);
        RemoteController server = (RemoteController) registry.lookup("busDao");
        server.addClient();
        System.out.println("RMI object found");

        while (true) {
            if (server.getNumberClients() == 2) {
                System.out.println("\nGet buses by root number");
                System.out.println(server.busesByRootNumber(1));

                System.out.println("\nGet buses that used more than given term");
                System.out.println(server.busesThatUsedMoreThanGivenTerm(2001));

                System.out.println("\nGet buses that drove more than given distance");
                System.out.println(server.busesThatDroveMoreThanGivenDistance(9999));
                break;
            }
        }
    }
}

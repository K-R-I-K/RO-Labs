package rmi.server;

import dao.Bus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteController extends Remote {
    int getNumberClients() throws RemoteException;
    void addClient() throws RemoteException;
    List<Bus> busesByRootNumber(int rootNumber) throws RemoteException;
    List<Bus> busesThatUsedMoreThanGivenTerm(int year) throws RemoteException;
    List<Bus> busesThatDroveMoreThanGivenDistance(int distance) throws RemoteException;
}

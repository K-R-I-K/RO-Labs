package rmi.server;

import dao.Bus;
import dao.BusDao;
import lombok.Synchronized;

import java.rmi.RemoteException;
import java.util.List;

public class RemoteControllerImpl implements RemoteController {
    private final BusDao busDao;
    private static volatile int numberOfClient = 0;

    public RemoteControllerImpl() throws RemoteException {
        this.busDao = new BusDao();
    }

    @Override
    public int getNumberClients() {
        return numberOfClient;
    }

    @Override
    public void addClient() {
        ++numberOfClient;
    }

    @Override
    @Synchronized("busDao")
    public List<Bus> busesByRootNumber(int rootNumber) throws RemoteException {
        return busDao.busesByRootNumber(rootNumber);
    }

    @Override
    @Synchronized("busDao")
    public List<Bus> busesThatUsedMoreThanGivenTerm(int year) throws RemoteException {
        return busDao.busesThatUsedMoreThanGivenTerm(year);
    }

    @Override
    @Synchronized("busDao")
    public List<Bus> busesThatDroveMoreThanGivenDistance(int distance) throws RemoteException {
        return busDao.busesThatDroveMoreThanGivenDistance(distance);
    }
}

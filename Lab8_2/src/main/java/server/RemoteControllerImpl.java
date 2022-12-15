package server;

import db.DBException;
import db.DBManager;
import db.entity.File;
import db.entity.Folder;
import lombok.Synchronized;

import java.rmi.RemoteException;
import java.util.List;

public class RemoteControllerImpl implements RemoteController {
    private final DBManager dbManager;
    private static volatile int numberOfClient = 0;

    public RemoteControllerImpl() throws RemoteException {
        this.dbManager = DBManager.getInstance();
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
    @Synchronized("dbManager")
    public Folder addNewFolder(Folder folder) throws RemoteException {
        try {
            return dbManager.insertFolder(folder);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public boolean deleteFolder(Folder folder) throws RemoteException {
        try {
            return dbManager.deleteFolder(folder);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public File addNewFileToFolder(File file, Folder folder) throws RemoteException {
        try {
            return dbManager.insertFileToFolder(file, folder);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public boolean deleteFile(File file, Folder folder) throws RemoteException {
        try {
            return dbManager.deleteFile(folder, file);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public boolean updateFile(File file) throws RemoteException {
        try {
            return dbManager.updateFile(file);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public boolean transferFile(File file, Folder folder) throws RemoteException {
        try {
            return dbManager.transferFile(file, folder);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public File copyFileToFolder(File file, Folder folder) throws RemoteException {
        try {
            return dbManager.insertFileToFolder(file, folder);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public List<Folder> getAllFolders() throws RemoteException {
        try {
            return dbManager.findAllFolders();
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Synchronized("dbManager")
    public List<File> getFilesInFolder(Folder folder) throws RemoteException {
        try {
            return dbManager.getFilesInFolder(folder);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }
}

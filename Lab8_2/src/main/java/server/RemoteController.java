package server;

import db.entity.File;
import db.entity.Folder;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteController extends Remote {
    int getNumberClients() throws RemoteException;
    void addClient() throws RemoteException;
    Folder addNewFolder(Folder folder) throws RemoteException;
    boolean deleteFolder(Folder folder) throws RemoteException;
    File addNewFileToFolder(File file, Folder folder) throws RemoteException;
    boolean deleteFile(File file, Folder folder) throws RemoteException;
    boolean updateFile(File file) throws RemoteException;
    boolean transferFile(File file, Folder folder) throws RemoteException;
    File copyFileToFolder(File file, Folder folder) throws RemoteException;
    List<Folder> getAllFolders() throws RemoteException;
    List<File> getFilesInFolder(Folder folder) throws RemoteException;
}

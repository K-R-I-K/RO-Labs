package main.model.db;

import main.model.db.implementations.DaoFactoryImpl;
import main.model.db.implementations.FileDao;
import main.model.db.implementations.FolderDao;

public abstract class DaoFactory {

    private static volatile DaoFactory daoFactory;

    public abstract FileDao createFileDao();

    public abstract FolderDao createFolderDao();

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if(daoFactory == null) {
                    daoFactory = new DaoFactoryImpl();
                }
            }
        }
        return daoFactory;
    }
}

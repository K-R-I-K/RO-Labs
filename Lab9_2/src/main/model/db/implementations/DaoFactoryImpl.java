package main.model.db.implementations;

import main.model.db.ConnectionPool;
import main.model.db.DaoFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoFactoryImpl extends DaoFactory {

    private final DataSource dataSource = ConnectionPool.getDataSource();
    private FileDao fileDao;
    private FolderDao folderDao;

    @Override
    public FileDao createFileDao() {
        if (fileDao == null) {
            fileDao = new FileDao(getConnection());
        }
        return fileDao;
    }

    @Override
    public FolderDao createFolderDao() {
        if (folderDao == null) {
            folderDao = new FolderDao(getConnection());
        }
        return folderDao;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

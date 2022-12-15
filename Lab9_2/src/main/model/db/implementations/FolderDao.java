package main.model.db.implementations;

import main.model.db.Fields;
import main.model.db.GenericDao;
import main.model.db.mapper.FolderMapper;
import main.model.entity.File;
import main.model.entity.Folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FolderDao implements GenericDao<Folder> {
    private final Connection connection;
    private final FolderMapper folderMapper;

    public FolderDao(Connection con) {
        this.connection = con;
        folderMapper = new FolderMapper();
    }

    @Override
    public void add(Folder entity) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO folders (name) VALUES(?)");
            statement.setString(1, entity.getName());
            statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Folder findById(Long id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM folders WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return folderMapper.extractFromResultSet(resultSet);
            }
            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Folder> findAll() {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM folders");
            ResultSet resultSet = statement.executeQuery();
            List<Folder> FolderList = new ArrayList<>();
            while(resultSet.next()){
                FolderList.add(folderMapper.extractFromResultSet(resultSet));
            }
            return FolderList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Folder entity) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Folders SET name=? WHERE id=?");
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM folders WHERE id=?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> getFilesInFolder(long id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM files " +
                    "WHERE files.folder_id=?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<File> filesList = new ArrayList<>();
            while (resultSet.next()){
                File buf = new File(resultSet.getInt(Fields.FILE_ID), resultSet.getString(Fields.FILE_NAME),
                        resultSet.getInt(Fields.FILE_SIZE), resultSet.getInt(Fields.FILE_FOLDER_ID));
                filesList.add(buf);
            }
            return filesList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}

package main.model.db.implementations;

import main.model.db.GenericDao;
import main.model.db.mapper.FileMapper;
import main.model.entity.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDao implements GenericDao<File> {
    private final Connection connection;
    private final FileMapper fileMapper;

    public FileDao(Connection con) {
        this.connection = con;
        fileMapper = new FileMapper();
    }

    @Override
    public void add(File entity) {
        try{
            PreparedStatement statement = connection.prepareStatement("INSERT INTO files (name, size, folder_id) VALUES(?, ?, ?)");
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getSize());
            statement.setLong(3, entity.getFolderId());
            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public File findById(Long id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM files WHERE id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new FileMapper().extractFromResultSet(resultSet);
            }
            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<File> findAll() {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM files");
            ResultSet resultSet = statement.executeQuery();
            List<File> filesList = new ArrayList<>();
            while(resultSet.next()){
                filesList.add(fileMapper.extractFromResultSet(resultSet));
            }
            return filesList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(File entity) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE files SET name=?, size=?, folder_id=? WHERE id=?");
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getSize());
            statement.setLong(3, entity.getFolderId());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM files WHERE id=?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

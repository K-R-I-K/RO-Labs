package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.entity.File;
import db.entity.Folder;


public class DBManager {

	Connection connection;
	public DBManager() {
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/file-system?user=postgres&password=1234");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	private static DBManager singleObject = null;
	public static synchronized DBManager getInstance() {
		if(singleObject == null){
			singleObject = new DBManager();
		}
		return singleObject;
	}
	public File getFile(int id) throws DBException {
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM files WHERE id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()){
				return new File(resultSet.getInt(Fields.FILE_ID), resultSet.getString(Fields.FILE_NAME),
						resultSet.getInt(Fields.FILE_SIZE), resultSet.getInt(Fields.FILE_FOLDER_ID));
			}
			return null;
		}catch (SQLException e){
			throw new DBException(e);
		}
	}
	public Folder getFolder(int id) throws DBException {
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM folders WHERE id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()){
				return new Folder(resultSet.getInt(Fields.FOLDER_ID), resultSet.getString(Fields.FOLDER_NAME));
			}
			return null;
		}catch (SQLException e){
			throw new DBException(e);
		}
	}
	public List<File> findAllFiles() throws DBException {
		try{
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM files");
			ResultSet resultSet = statement.executeQuery();
			List<File> filesList = new ArrayList<>();
			while(resultSet.next()){
				File file = new File(resultSet.getInt(Fields.FILE_ID), resultSet.getString(Fields.FILE_NAME),
						resultSet.getInt(Fields.FILE_SIZE), resultSet.getInt(Fields.FILE_FOLDER_ID));
				filesList.add(file);
			}
			return filesList;
		}catch (SQLException e){
			throw new DBException(e);
		}
	}
	public List<Folder> findAllFolders() throws DBException {
		try{
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM folders");
			ResultSet resultSet = statement.executeQuery();
			List<Folder> FolderList = new ArrayList<>();
			while(resultSet.next()){
				Folder buf = new Folder(resultSet.getInt(Fields.FOLDER_ID), resultSet.getString(Fields.FOLDER_NAME));
				FolderList.add(buf);
			}
			return FolderList;
		}catch (SQLException e){
			throw new DBException(e);
		}
	}
	public boolean insertFile(File file) throws DBException {
		if (file == null)
			return false;
		try{
			PreparedStatement statement = connection.prepareStatement("INSERT INTO files (name, size, folder_id) VALUES(?, ?, ?)");
			statement.setString(1, file.getName());
			statement.setInt(2, file.getSize());
			statement.setInt(3, file.getFolderId());
			statement.executeUpdate();
			return true;
		}catch (SQLException e){
			throw new DBException(e);
		}
	}
	public boolean insertFolder(Folder folder) throws DBException {
		if (folder == null)
			return false;
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO folders (name) VALUES(?)");
			statement.setString(1, folder.getName());
			statement.executeUpdate();
			return true;
		} catch (SQLException e){
			throw new DBException(e);
		}
	}
	public boolean updateFile(File file) throws DBException {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE files SET name=?, size=?, folder_id=? WHERE id=?");
			statement.setString(1, file.getName());
			statement.setInt(2, file.getSize());
			statement.setInt(3, file.getFolderId());
			statement.setInt(4, file.getId());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public boolean updateFolder(Folder folder) throws DBException {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE Folders SET name=? WHERE id=?");
			statement.setString(1, folder.getName());
			statement.setInt(2, folder.getId());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public boolean deleteFiles(File... files) throws DBException {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM files WHERE name=? AND size=? AND folder_id=?");
			for (int i = 0; i < files.length; i++) {
				statement.setString(1, files[i].getName());
				statement.setInt(2, files[i].getSize());
				statement.setInt(3, files[i].getFolderId());
				statement.executeUpdate();
			}
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public boolean deleteFolder(Folder folder) throws DBException {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM folders WHERE name=?");
			statement.setString(1, folder.getName());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public List<File> getFilesInFolder(Folder folder) throws DBException {
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
					"FROM files " +
					"WHERE files.folder_id=?");
			preparedStatement.setInt(1, folder.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			List<File> filesList = new ArrayList<>();
			while (resultSet.next()){
				File buf = new File(resultSet.getInt(Fields.FILE_ID), resultSet.getString(Fields.FILE_NAME),
						resultSet.getInt(Fields.FILE_SIZE), resultSet.getInt(Fields.FILE_FOLDER_ID));
				filesList.add(buf);
			}
			return filesList;
		}catch (SQLException e){
			throw new DBException(e);
		}
	}
}

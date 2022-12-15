package main.model.db.mapper;

import main.model.entity.File;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileMapper implements Mapper<File> {

    public File extractFromResultSet(ResultSet rs) throws SQLException {
        return File.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .size(rs.getLong("size"))
                .folderId(rs.getLong("folder_id"))
                .build();
    }
}

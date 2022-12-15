package main.model.db.mapper;

import main.model.entity.Folder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderMapper implements Mapper<Folder> {

    public Folder extractFromResultSet(ResultSet rs) throws SQLException {
        return Folder.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}

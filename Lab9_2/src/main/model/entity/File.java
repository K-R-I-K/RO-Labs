package main.model.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class File {
	private long id;
	private String name;
	private long size;
	private long folderId;
	public File(long id, String name, long size, long folderId) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.folderId = folderId;
	}
	public String toString(){
		return name + " " + size;
	}
}
package db.entity;

import java.io.Serial;
import java.io.Serializable;

public class File implements Serializable {
	private int id;
	private String name;
	private int size;
	private int folderId;

	@Serial
	private static final long serialVersionUID = 1234567L;
	public File(int id, String name, int size, int folderId) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.folderId = folderId;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getSize() {
		return size;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}

	public String toString(){
		return name + " " + size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		File file = (File) o;
		return name.equals(file.name);
	}

	public static File createFile(String name, int size, int folderId){
		return new File(0, name, size, folderId);
	}

}
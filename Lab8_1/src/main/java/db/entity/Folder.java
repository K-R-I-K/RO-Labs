package db.entity;

import java.io.Serial;
import java.io.Serializable;

public class Folder implements Serializable {
	private final int id;
	private String name;
	@Serial
	private static final long serialVersionUID = 7654321L;
	public Folder(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Folder folder = (Folder) o;
		return name.equals(folder.name);
	}
	public static Folder createFolder(String name) {
		return new Folder(0, name);
	}

}
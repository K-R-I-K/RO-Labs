package entity;

public class File {
	private String id;
	private String name;
	private int size;
	private String folderId;
	public File(String id, String name, int size, String folderId) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.folderId = folderId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getSize() {
		return size;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
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
}
package db.entity;

public class File {
	private final int id;
	private String name;
	private int size;
	private int folderId;
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
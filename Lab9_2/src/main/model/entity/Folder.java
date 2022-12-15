package main.model.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Folder {
	private long id;
	private String name;

	public Folder(long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
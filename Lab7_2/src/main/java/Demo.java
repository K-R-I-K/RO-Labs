import db.DBException;
import db.DBManager;
import db.entity.File;
import db.entity.Folder;

public class Demo {

	public static void main(String[] args) throws DBException {
		DBManager dbManager = DBManager.getInstance();

		File file1 = dbManager.getFile(1);
		File file3 = dbManager.getFile(3);
		File file6 = dbManager.getFile(6);

		Folder folder1 = dbManager.getFolder(1);
		Folder folder2 = dbManager.getFolder(2);
		Folder folder3 = dbManager.getFolder(3);

		System.out.println("\n get file");
		System.out.println(file1); // 1
		System.out.println(file3); // 3
		System.out.println(file6); // 6

		System.out.println("\n get folder");
		System.out.println(folder1); // 1
		System.out.println(folder2); // 2
		System.out.println(folder3); // 3

		System.out.println("\n find all");
		System.out.println(dbManager.findAllFiles()); // 1 2 3 4 5 6
		System.out.println(dbManager.findAllFolders()); // 1 2 3

		System.out.println("\n insert");
		dbManager.insertFolder(Folder.createFolder("folder4"));
		Folder folder4 = dbManager.getFolder(4);
		dbManager.insertFile(File.createFile("file7", 70, 4));
		File file7 = dbManager.getFile(7);
		System.out.println(dbManager.findAllFiles()); // 1 2 3 4 5 6 7
		System.out.println(dbManager.findAllFolders()); // 1 2 3 4

		System.out.println("\n update");
		folder4.setName("folder5");
		dbManager.updateFolder(folder4);
		folder4 = dbManager.getFolder(4);
		file3.setFolderId(1);
		dbManager.updateFile(file3);
		file3 = dbManager.getFile(3);
		System.out.println(folder1.toString() + (dbManager.getFilesInFolder(folder1))); // 1 | 1 3
		System.out.println(folder2.toString() + (dbManager.getFilesInFolder(folder2))); // 2 | 2
		System.out.println(folder4.toString() + (dbManager.getFilesInFolder(folder4))); // 5 | 7

		System.out.println("\n delete");
		dbManager.deleteFolder(folder4);
		dbManager.deleteFiles(file6);
		System.out.println(dbManager.findAllFiles()); // 1 2 4 5 3
		System.out.println(dbManager.findAllFolders()); // 1 2 3

		System.out.println("\n files in folder");
		System.out.println(folder1.toString() + (dbManager.getFilesInFolder(folder1))); // 1 | 1 3
		System.out.println(folder2.toString() + (dbManager.getFilesInFolder(folder2))); // 2 | 2
		System.out.println(folder3.toString() + (dbManager.getFilesInFolder(folder3))); // 3 | 4 5
	}

}

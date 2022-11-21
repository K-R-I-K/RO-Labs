import entity.File;
import entity.Folder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Demo {
	private final static String path = "src/main/java/XML/FileSystem.xml";

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
		FileSystem fileSystem = DOMParser.parse(path);
		fileSystem.print();

		System.out.println("\nAdd a new Folder");
		Folder folder = new Folder("fo", "myFolder1");
		fileSystem.addNewFolder(folder);
		fileSystem.print();

		System.out.println("\nAdd some new Files");
		entity.File file1 = new entity.File("fi", "myFile1", 10, "fo1");
		fileSystem.addNewFile(file1, folder.getId());
		entity.File file2 = new entity.File("fi", "myFile2", 20, "fo1");
		fileSystem.addNewFile(file2, folder.getId());
		fileSystem.print();

		System.out.println("\nChange File's name");
		fileSystem.renameFile(fileSystem.getFile("fi1"), "newFile");
		fileSystem.print();

		System.out.println("\nChange Folder's name");
		fileSystem.renameFolder(fileSystem.getFolder("fo1"), "newFolder");
		fileSystem.print();

		System.out.println("\nGet files");
		List<File> files = fileSystem.getFolderFiles("fo1");
		System.out.println(files);
		fileSystem.print();

		System.out.println("\nGet all folders\n");
		Map<String, Folder> folders = fileSystem.getFolders();
		System.out.println(folders);
		fileSystem.print();

		System.out.println("\nDelete a file\n");
		fileSystem.removeFile(file1);
		fileSystem.print();

		System.out.println("\nDelete folder\n");
		fileSystem.removeFolder(folder);
		fileSystem.print();

		DOMParser.write(fileSystem, "src/main/java/XML/out.xml");
	}
}

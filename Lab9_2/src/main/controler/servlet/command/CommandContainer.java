package main.controler.servlet.command;

import main.model.db.DaoFactory;
import main.model.db.implementations.FileDao;
import main.model.db.implementations.FolderDao;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private static final Map<String, Command> commands;

    private static final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final FileDao fileDao = daoFactory.createFileDao();
    private static final FolderDao folderDao = daoFactory.createFolderDao();


    private CommandContainer() {
    }

    static {
        commands = new HashMap<>();
        commands.put("getFolders", new GetFolders(folderDao));
        commands.put("getFilesInFolder", new GetFilesInFolder(folderDao));
        commands.put("deleteFolder", new DeleteFolder(folderDao));
        commands.put("deleteFile", new DeleteFile(fileDao));
        commands.put("addFolder", new AddFolder(folderDao));
        commands.put("getFileForm", new GetFileForm(folderDao));
        commands.put("addFile", new AddFile(fileDao, folderDao));
        commands.put("getEditFolder", new GetEditFolder(folderDao));
        commands.put("editFolder", new EditFolder(folderDao));
        commands.put("getEditFile", new GetEditFile(fileDao));
        commands.put("editFile", new EditFile(fileDao));
    }

    public static Command getCommand(String command) {
        return commands.get(command);
    }

    public static String doCommand(Command command, HttpServletRequest request) {
        return command.execute(request);
    }
}

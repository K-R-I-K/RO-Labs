package main.controler.servlet.command;

import main.model.db.implementations.FolderDao;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class GetFolders implements Command{
    private FolderDao folderDao;
    public GetFolders(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        List<Folder> folders = folderDao.findAll();

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("folders", folders);
        return "folders.jsp";
    }
}

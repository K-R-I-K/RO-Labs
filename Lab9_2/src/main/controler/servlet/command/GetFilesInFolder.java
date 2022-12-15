package main.controler.servlet.command;

import main.model.db.implementations.FolderDao;
import main.model.entity.File;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class GetFilesInFolder implements Command {
    private FolderDao folderDao;
    public GetFilesInFolder(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        List<File> files = folderDao.getFilesInFolder(Long.parseLong(request.getParameter("id")));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("files", files);
        return "files.jsp";
    }
}

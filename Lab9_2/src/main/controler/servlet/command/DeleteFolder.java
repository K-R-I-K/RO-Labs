package main.controler.servlet.command;

import main.model.db.implementations.FolderDao;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class DeleteFolder implements Command {
    private FolderDao folderDao;
    public DeleteFolder(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        folderDao.delete(Long.parseLong(request.getParameter("id")));
        return "index.jsp";
    }
}

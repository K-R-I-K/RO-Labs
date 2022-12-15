package main.controler.servlet.command;

import main.model.db.implementations.FolderDao;
import main.model.entity.File;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class GetEditFolder implements Command {
    private FolderDao folderDao;
    public GetEditFolder(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        Folder editFolder = folderDao.findById(Long.parseLong(request.getParameter("id")));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("editFolder", editFolder);
        return "redirect:editFolder.jsp";
    }
}

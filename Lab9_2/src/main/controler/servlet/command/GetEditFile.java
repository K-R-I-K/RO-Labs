package main.controler.servlet.command;

import main.model.db.implementations.FileDao;
import main.model.db.implementations.FolderDao;
import main.model.entity.File;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GetEditFile implements Command {
    private FileDao fileDao;
    public GetEditFile(FileDao fileDao) {
        this.fileDao = fileDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        File editFile = fileDao.findById(Long.parseLong(request.getParameter("id")));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("editFile", editFile);
        return "editFile.jsp";
    }
}

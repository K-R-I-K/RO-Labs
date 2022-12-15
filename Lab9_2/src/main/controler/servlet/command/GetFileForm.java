package main.controler.servlet.command;

import main.model.db.implementations.FileDao;
import main.model.db.implementations.FolderDao;
import main.model.entity.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class GetFileForm implements Command {
    private FolderDao folderDao;
    public GetFileForm(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("folders", folderDao.findAll());
        return "newFile.jsp";
    }
}

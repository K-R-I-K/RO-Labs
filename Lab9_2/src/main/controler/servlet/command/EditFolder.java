package main.controler.servlet.command;

import main.model.db.implementations.FolderDao;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;

public class EditFolder implements Command {
    private FolderDao folderDao;
    public EditFolder(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        folderDao.update(Folder.builder()
                .id(Long.parseLong(request.getParameter("id")))
                .name(String.valueOf(request.getParameter("name")))
                .build());
        return "redirect:index.jsp";
    }
}

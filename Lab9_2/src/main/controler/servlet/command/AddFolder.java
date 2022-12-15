package main.controler.servlet.command;

import main.model.db.implementations.FolderDao;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class AddFolder implements Command {
    private FolderDao folderDao;
    public AddFolder(FolderDao folderDao) {
        this.folderDao = folderDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        folderDao.add(Folder.builder()
                        .id(0)
                        .name(String.valueOf(request.getParameter("name")))
                                .build());
        return "redirect:index.jsp";
    }
}

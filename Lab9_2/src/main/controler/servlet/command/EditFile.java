package main.controler.servlet.command;

import main.model.db.implementations.FileDao;
import main.model.db.implementations.FolderDao;
import main.model.entity.File;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;

public class EditFile implements Command {
    private FileDao fileDao;
    public EditFile(FileDao fileDao) {
        this.fileDao = fileDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        fileDao.update(File.builder()
                .id(Long.parseLong(request.getParameter("id")))
                .name(String.valueOf(request.getParameter("name")))
                .size(Long.parseLong(request.getParameter("size")))
                .folderId(Long.parseLong(request.getParameter("folderId")))
                .build());
        return "redirect:index.jsp";
    }
}

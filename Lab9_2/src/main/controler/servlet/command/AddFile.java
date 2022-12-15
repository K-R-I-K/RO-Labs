package main.controler.servlet.command;

import main.model.db.implementations.FileDao;
import main.model.db.implementations.FolderDao;
import main.model.entity.File;
import main.model.entity.Folder;

import javax.servlet.http.HttpServletRequest;

public class AddFile implements Command {
    private FileDao fileDao;
    public AddFile(FileDao fileDao, FolderDao folderDao) {
        this.fileDao = fileDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        fileDao.add(File.builder()
                .id(0)
                .name(String.valueOf(request.getParameter("name")))
                        .size(Long.parseLong(request.getParameter("size")))
                        .folderId(Long.parseLong(request.getParameterValues("folders")[0]))
                .build());
        return "redirect:folders.jsp";
    }
}

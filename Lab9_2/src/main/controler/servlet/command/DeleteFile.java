package main.controler.servlet.command;

import main.model.db.implementations.FileDao;

import javax.servlet.http.HttpServletRequest;

public class DeleteFile implements Command {
    private FileDao fileDao;
    public DeleteFile(FileDao fileDao) {
        this.fileDao = fileDao;
    }
    @Override
    public String execute(HttpServletRequest request) {
        fileDao.delete(Long.parseLong(request.getParameter("id")));
        return "index.jsp";
    }
}

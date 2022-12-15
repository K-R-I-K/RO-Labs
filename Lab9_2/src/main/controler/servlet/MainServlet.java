package main.controler.servlet;

import main.controler.servlet.command.Command;
import main.controler.servlet.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet("/home")
public class MainServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String page = "index.jsp";

        String commandName = request.getParameter("command");

        if(commandName == null) {
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            Command command = CommandContainer.getCommand(commandName);
            page = CommandContainer.doCommand(command, request);

            if (page.contains("redirect")) {
                response.sendRedirect(page.replace("redirect:",""));
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        }
    }

}

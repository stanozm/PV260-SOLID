package pv260.solid.srp.solution;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfirmationServlet extends HttpServlet {

    private final ResourceLoader loader;

    public ConfirmationServlet() {
        //obtained via dependency injection
        this.loader = new ResourceLoader();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                   IOException {
        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            out.println(constructPage(request.getParameter("order"),
                                      request.getParameter("cost")));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    private String constructPage(String order,
                                 String price) throws Exception {
        String template = this.loader.loadResource(getClass(),
                                                   "confirm.html");

        return template.replace("{ORDER}",
                                order)
                       .replace("{COST}",
                                price);
    }
}

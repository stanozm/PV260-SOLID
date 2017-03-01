package pv260.solid.srp.original;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.compile;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfirmationServlet extends HttpServlet {

    private static final Pattern COST_REGEX = compile("order=([\\w]+)&cost=([\\w]+)");

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                   IOException {

        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println(constructPage(request.getQueryString()));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    private String constructPage(String query) throws Exception {
        try (InputStream in = getClass().getResourceAsStream("confirm.html");
                Scanner scan = new Scanner(in).useDelimiter("\\A")) {
            String template = scan.next();

            Matcher costMatcher = COST_REGEX.matcher(query);
            costMatcher.find();

            return template.replace("{ORDER}",
                                    costMatcher.group(1))
                           .replace("{COST}",
                                    costMatcher.group(2));
        }
    }
}

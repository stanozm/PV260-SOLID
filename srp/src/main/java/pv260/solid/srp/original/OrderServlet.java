package pv260.solid.srp.original;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.compile;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderServlet extends HttpServlet {

    private static final Pattern ID_COST_REGEX = compile("user=([\\w]+)&cost=([\\w]+)");

    private long orderIdGen = 0;

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                   IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println(constructPage(obtainProducts()));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    private List<Product> obtainProducts() {
        //this would come from some other service,
        //could be a DB query or remote API call etc.
        return asList(new Product(1,
                                  "Teddy Bear",
                                  15),
                      new Product(2,
                                  "Toy Car",
                                  25),
                      new Product(3,
                                  "Bike",
                                  119),
                      new Product(4,
                                  "Ball",
                                  10));
    }

    private String constructPage(List<Product> products) throws Exception {
        try (InputStream in = getClass().getResourceAsStream("order.html");
                Scanner scan = new Scanner(in).useDelimiter("\\A")) {
            String template = scan.next();
            StringBuilder productsFormatted = new StringBuilder();
            for (Product p : products) {
                productsFormatted.append(formatProduct(p));
            }
            return template.replace("{PRODUCTS}",
                                    productsFormatted.toString());
        }
    }

    private String formatProduct(Product product) {
        return "<tr><td>" + product.getId() + "</td><td>" + product.getName() + "</td><td>" + product.getPrice()
                + "</td></tr>\n";
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException,
                                                       IOException {
        String content;
        try (InputStream in = request.getInputStream(); Scanner scan = new Scanner(in).useDelimiter("\\A")) {
            content = scan.next();
        }
        response.setContentType("text/html");
        try {
            Matcher idCostMatcher = ID_COST_REGEX.matcher(content);
            idCostMatcher.find();

            long orderId = this.orderIdGen++;
            long userId = Long.valueOf(idCostMatcher.group(1));
            long orderPrice = Long.valueOf(idCostMatcher.group(2));
            System.out.println(format("Saving to DB: user %s, order %s, cost %s",
                                      userId,
                                      orderId,
                                      orderPrice));

            response.sendRedirect(format("%s/confirm?order=%s&cost=%s",
                                         //this would not be hardcoded but rather come from context
                                         //because of how we deployed the servlet, context is null
                                         "http://localhost:8080",
                                         orderId,
                                         orderPrice));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}

package pv260.solid.srp.solution;

import java.io.*;
import static java.lang.String.format;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//curl -X POST --data "id=1&cost=0" localhost:8080/

public class OrderServlet extends HttpServlet {

    private final ProductTableFormatter formatter;

    private final ResourceLoader loader;

    private final ProductSource products;

    private final PriceCalculator priceCalc;

    private long orderIdGen = 0;

    public OrderServlet() {
        //this would be injected by container
        this.formatter = new ProductTableFormatter();
        this.loader = new ResourceLoader();
        this.products = new ProductSource();
        this.priceCalc = new PriceCalculator();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                   IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println(constructPage(this.products.allProducts()));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException,
                                                       IOException {
        try {
            long orderId = this.orderIdGen++;
            int userId = Integer.valueOf(request.getParameter("user"));
            int productId = Integer.valueOf(request.getParameter("product"));
            int quantity = Integer.valueOf(request.getParameter("quantity"));
            Product selectedProduct = this.products.productById(productId);
            if (quantity <= 0 || selectedProduct == null) {
                response.sendError(422);
                return;
            }
            BigDecimal price = this.priceCalc.calculatePrice(selectedProduct,
                                                             quantity);

            System.out.println(format("Saving to DB: user %s, order %s, cost %s",
                                      userId,
                                      orderId,
                                      price));

            response.sendRedirect(format("%s/confirm?order=%s&cost=%s",
                                         "http://localhost:8080",
                                         orderId,
                                         price));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }

    private String constructPage(List<Product> products) throws Exception {
        String template = this.loader.loadResource(getClass(),
                                                   "order.html");

        StringBuilder productsFormatted = new StringBuilder();
        for (Product p : products) {
            productsFormatted.append(this.formatter.formatProduct(p));
        }
        return template.replace("{PRODUCTS}",
                                productsFormatted.toString());
    }
}

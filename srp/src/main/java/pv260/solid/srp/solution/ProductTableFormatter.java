package pv260.solid.srp.solution;

public class ProductTableFormatter {

    public String formatProduct(Product product) {
        return "<tr><td>" + product.getId() + "</td><td>" + product.getName() + "</td><td>" + product.getPrice()
                + "</td></tr>\n";
    }

}

package pv260.solid.srp.solution;

import java.math.BigDecimal;
import static java.util.Arrays.asList;
import java.util.List;

public class ProductSource {

    public List<Product> allProducts() {
        return asList(new Product(1,
                                  "Teddy Bear",
                                  new BigDecimal("15")),
                      new Product(2,
                                  "Toy Car",
                                  new BigDecimal("25")),
                      new Product(3,
                                  "Bike",
                                  new BigDecimal("119")),
                      new Product(4,
                                  "Ball",
                                  new BigDecimal("10")));
    }

    public Product productById(long id) {
        //this is not they way this method would be implemented
        //if we were using real database here,
        //done this way here for brewity
        for (Product p : allProducts()) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("No such product: " + id);
    }
}

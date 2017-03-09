package pv260.solid.srp.solution;

import java.math.BigDecimal;

public class PriceCalculator {

    public BigDecimal calculatePrice(Product product,
                                     int quantity) {
        return product.getPrice()
                      .multiply(new BigDecimal(quantity));
    }

}

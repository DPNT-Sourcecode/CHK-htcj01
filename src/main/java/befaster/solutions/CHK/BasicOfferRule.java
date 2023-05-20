package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

public record BasicOfferRule(String sku, Integer quantity) implements IOfferRule {
    @Override
    public Boolean isSatisfiedBy(OrderUnit unit) {
        return unit.getQuantity() >= quantity;
    }
}




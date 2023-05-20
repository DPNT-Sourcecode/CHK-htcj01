package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

public class GroupOfferRule implements IOfferRule {

    private final List<String> skus = new ArrayList<>();
    private Integer quantity;

    public GroupOfferRule(Integer quantity, String... ids) {
        this.quantity = quantity;
        this.skus.addAll(List.of(ids));
    }

    @Override
    public Integer quantity() {
        return null;
    }

    @Override
    public String sku() {
        return null;
    }

    public List<String> getSkus() {
        return skus;
    }

    @Override
    public Boolean isSatisfiedBy(OrderUnit unit) {
        return isSatisfiedBy(List.of(unit));
    }

    @Override
    public Orderun isSatisfiedBy(List<OrderUnit> units) {
        return units.stream().filter(skus::contains).mapToInt(OrderUnit::getQuantity).sum() >= quantity;
    }
}

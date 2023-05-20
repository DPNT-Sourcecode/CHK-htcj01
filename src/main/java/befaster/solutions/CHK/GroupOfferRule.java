package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return isSatisfiedBy(List.of(unit)).isSatisfied();
    }

    @Override
    public RuleCheckResult isSatisfiedBy(List<OrderUnit> units) {
        List<OrderUnit> matched = new ArrayList<>();
        List<OrderUnit> unmatched = new ArrayList<>();

        units.forEach(unit -> {
            if (skus.contains(unit.getSku()) && unit.getQuantity() > 0){
                matched.add(unit);
            } else {
                unmatched.add(unit);
            }
        });
        return new RuleCheckResult(matched, unmatched);
    }
}



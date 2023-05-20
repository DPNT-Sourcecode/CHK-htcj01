package befaster.solutions.CHK;

public record BasicOfferRule(String sku, Integer quantity) implements IOfferRule {
    @Override
    public Boolean isSatisfiedBy(OrderUnit unit) {
        return unit.getQuantity() >= quantity;
    }
}

public record GroupOfferRule() implements  IOfferRule {

}


package befaster.solutions.CHK;

public interface IOfferRule {
    Integer quantity();

    String sku();

    Boolean isSatisfiedBy(OrderUnit unit);
}


package befaster.solutions.CHK;

public interface IOfferRule {
    Integer getQuantity();

    String getSku();

    Boolean isSatisfiedBy(OrderUnit unit);
}

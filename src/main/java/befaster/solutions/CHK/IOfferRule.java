package befaster.solutions.CHK;

import java.util.List;

public interface IOfferRule {
    Integer quantity();

    String sku();

    Boolean isSatisfiedBy(OrderUnit unit);
    RuleCheckResult isSatisfiedBy(List<OrderUnit> units);
}


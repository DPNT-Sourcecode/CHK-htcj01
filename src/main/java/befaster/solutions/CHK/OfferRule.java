package befaster.solutions.CHK;

public class OfferRule {
    private final String sku;
    private final Integer quantity;

    public OfferRule(String sku, Integer quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSku() {
        return sku;
    }

    /**
     * Method to identify if this specific condition is satisfied by the SKUs
     * Note: To be able to match a Condition twice, the Offer should be able to subtract the skus size.
     * This way we can apply the check recursively.
     *
     * @param unit
     * @return
     */
    Boolean isSatisfiedBy(OrderUnit unit) {
        return unit.getQuantity() >= quantity;
    }
}

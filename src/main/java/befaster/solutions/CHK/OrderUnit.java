package befaster.solutions.CHK;

/**
 * Class to represent the SKU and quantity being ordered.
 */
class OrderUnit {
    private final String sku;
    private final Integer quantity;

    private final Integer price;

    private Offer matchedOffer = null;

    public OrderUnit(String sku, Integer quantity, Integer price) {
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSku() {
        return sku;
    }

    public Integer getTotal() {
        return computeTotal(this.matchedOffer);
    }

    public Integer computeTotal(Offer offer) {
        Integer offerTotal = 0;
        Integer timesAffected = 0;
        Integer remainingQuantity = this.quantity;
        if (offer != null) {
            offerTotal = offer != null ? offer.getFinalPrice() : 0;
            timesAffected = (this.quantity / offer.getQuantity());
            remainingQuantity = this.quantity % offer.getQuantity();
        }
        return (offerTotal * timesAffected) + (remainingQuantity * price);
    }

    public void setMatchedOffer(Offer matchedOffer) {
        if (this.matchedOffer != null) {
            Integer total = getTotal();
            Integer candidate = computeTotal(matchedOffer);
            if (candidate < total) {
                this.matchedOffer = matchedOffer;
            }
        } else {
            this.matchedOffer = matchedOffer;
        }
    }
}

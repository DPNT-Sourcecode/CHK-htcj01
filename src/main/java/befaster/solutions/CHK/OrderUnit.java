package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the SKU and quantity being ordered.
 */
class OrderUnit {
    private final String sku;
    private final Integer quantity;

    private final Integer price;

    private Offer matchedOffer = null;
    private List<Discount> discounts = new ArrayList<>();

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

    public Integer getPrice() {
        return price;
    }

    public Integer getTotal() {
        return computeTotal(this.matchedOffer);
    }

    public Integer computeTotal(Offer offer) {
        if (offer != null && offer.isDynamic()) {
            return offer.computeFinalPrice(new OfferContext(this, offer));
        }
        Integer fullTotal = this.quantity * this.getPrice();
        Integer offerTotal = 0;
        Integer timesAffected = 0;
        Integer remainingQuantity = this.quantity;
        if (offer != null) {
            timesAffected = (this.quantity / offer.getQuantity());
            offerTotal = (offer != null ? offer.getFinalPrice() : 0) * timesAffected;
            remainingQuantity = this.quantity % offer.getQuantity();
        }

        Integer discount = 0;
         if (this.discounts != null) {
             discount = this.discounts.stream().mapToInt(Discount::getValue).sum();
         }

         if (discounts.size() > 0 && discount <= offerTotal) {
             Integer totalWithDiscount = fullTotal + discount;
            return totalWithDiscount < 0? 0 : totalWithDiscount;
         }
         return offerTotal + (remainingQuantity * price);
    }

    public void addDiscount(Discount discount){
        this.discounts.add(discount);
    }

    public boolean setMatchedOffer(Offer matchedOffer) {
        if (this.matchedOffer != null) {
            Integer total = getTotal();
            Integer candidate = computeTotal(matchedOffer);
            if (candidate < total) {
                this.matchedOffer = matchedOffer;
                return true;
            }
        } else {
            this.matchedOffer = matchedOffer;
            return true;
        }
        return false;
    }
}



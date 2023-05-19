package befaster.solutions.CHK;

import java.util.List;
import java.util.function.Function;

/**
 * A class to represent an Offer
 * <p>
 * An Offer is composed by multiple OfferCondition,
 * this way is possible to create an Offer able to match a bundle of different SKUs.
 */
class Offer {

    private final String sku;
    private final OfferRule rule;
    private final Integer finalPrice;

    private final Function<OfferContext, Integer> dynamicPriceFN;

    public Offer(String sku, OfferRule rule, Integer finalPrice) {
        this.sku = sku;
        this.rule = rule;
        this.finalPrice = finalPrice;
        this.dynamicPriceFN = null;
    }

    public Offer(String sku, OfferRule rule, Function<OfferContext, Integer> dynamicPriceFN) {
        this.sku = sku;
        this.rule = rule;
        this.dynamicPriceFN = dynamicPriceFN;
        this.finalPrice = null;
    }


    public List<Discount> computeDiscounts(OfferContext ctx) {

    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return rule.getQuantity();
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public Integer computeFinalPrice(OfferContext context) {
        return this.dynamicPriceFN.apply(context);
    }

    public boolean isDynamic() {
        return this.dynamicPriceFN != null;
    }

    public Boolean isSatisfiedBy(OrderUnit unit) {
        return this.rule.isSatisfiedBy(unit);
    }
}



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

    private final Function<OfferContext, List<Discount>> computeDiscountFN;

    public Offer(String sku, OfferRule rule, Integer finalPrice, Function<OfferContext, List<Discount>> computeDiscountFN) {
        this.sku = sku;
        this.rule = rule;
        this.finalPrice = finalPrice;
        this.computeDiscountFN = computeDiscountFN;
        this.dynamicPriceFN = null;
    }

    public Offer(String sku, OfferRule rule, Function<OfferContext, Integer> dynamicPriceFN, Function<OfferContext, List<Discount>> computeDiscountFN) {
        this.sku = sku;
        this.rule = rule;
        this.dynamicPriceFN = dynamicPriceFN;
        this.computeDiscountFN = computeDiscountFN;
        this.finalPrice = null;
    }

    public List<Discount> computeDiscounts(OfferContext ctx) {
        return computeDiscounts(ctx);
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



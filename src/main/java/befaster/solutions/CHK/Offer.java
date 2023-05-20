package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class to represent an Offer
 * <p>
 * An Offer is composed by multiple OfferCondition,
 * this way is possible to create an Offer able to match a bundle of different SKUs.
 */
class Offer {

    private final String sku;
    private final IOfferRule rule;
    private final Integer finalPrice;
    private final Function<OfferContext, Integer> dynamicPriceFN;

    private final Function<OfferContext, List<Discount>> computeDiscountFN;

    public Offer(String sku, IOfferRule rule, Integer finalPrice, Function<OfferContext, List<Discount>> computeDiscountFN) {
        this.sku = sku;
        this.rule = rule;
        this.finalPrice = finalPrice;
        this.computeDiscountFN = computeDiscountFN;
        this.dynamicPriceFN = null;
    }

    public Offer(String sku, BasicOfferRule rule, Function<OfferContext, Integer> dynamicPriceFN, Function<OfferContext, List<Discount>> computeDiscountFN) {
        this.sku = sku;
        this.rule = rule;
        this.dynamicPriceFN = dynamicPriceFN;
        this.computeDiscountFN = computeDiscountFN;
        this.finalPrice = null;
    }

    public List<Discount> computeDiscounts(OfferContext ctx) {
        return computeDiscountFN != null?  computeDiscountFN.apply(ctx) : new ArrayList<>();
    }

    public String getSku() {
        return sku;
    }

    public Boolean isGroupOffer() {
        return this.rule instanceof GroupOfferRule;
    }

    public Integer getQuantity() {
        return rule.quantity();
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

    public OrderUnit extractBundle(List<OrderUnit> units) {
        GroupOfferRule offerRule = (GroupOfferRule) this.rule;
        List<OrderUnit> orderUnits = units.stream().filter(unit -> this.rule.isSatisfiedBy(unit)).collect(Collectors.toList());
        String name = orderUnits.stream().map(OrderUnit::getSku).collect(Collectors.joining());
        //TODO: WIP
        return new OrderUnit(name, 3, 45);
    }
}

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

    /**
     *
     * @param units
     * @return
     */
    //TODO: Should return a list because it can be applied to multiple combinations.
    //What about return discounts? The discounts can be applied in the end
    //Anyway we need to return the Order units with their sizes changed.
    public OfferBundleResult extractBundle(List<OrderUnit> units) {
        OfferBundleResult response = new OfferBundleResult();
        /*
        Steps:
            - Iterate over the units until don't be satisfied (recursively)
              - When a discount is applied we should subtract the size and run it again
            - Return a list of units covering the combination of items to apply the offer (Maybe return discounts?)
         */
        GroupOfferRule offerRule = (GroupOfferRule) this.rule;
        RuleCheckResult checkResult = offerRule.isSatisfiedBy(units);
        if (checkResult.isSatisfied()) {
            //Should subtract the original units and extractBundles again
            AffectedOrderUnitsResult result = computeAffectedOrderUnits(checkResult.getMatched(), checkResult);
            response.add(result.unit);
            response.merge(extractBundle(result.remaining));
        }
        return response;
    }

    /*
        Should:
            - Iterate over matched units
              - accumulate the quantity from the matched up to be higher than the expected quantity.
                - When is higher, subtract the quantity from the acc and run the same method with the remaining.
    */
    private AffectedOrderUnitsResult computeAffectedOrderUnits(List<OrderUnit> units, RuleCheckResult checkResult){
        List<OrderUnit> matched = checkResult.getMatched();
        List<OrderUnit> acc = new ArrayList<>();
        List<OrderUnit> bundles = new ArrayList<>();
        List<OrderUnit> remaining = new ArrayList<>();
        Integer counter = 0;
        Integer expectedQuantity = this.rule.quantity();
        for (OrderUnit unit: matched) {
            if (counter + unit.getQuantity() <= expectedQuantity){
                acc.add(unit);
                counter += unit.getQuantity();
            }
        }

        int currentQuantity = 0;
        String unitName = "";
        while (currentQuantity < expectedQuantity) {
            for (OrderUnit unit : acc) {
                currentQuantity ++;
                unitName+=unit.getSku();

                OrderUnit orderUnit = unit.subtract(1);
                if (orderUnit.getQuantity() > 0) {
                    remaining.add(orderUnit);
                }
            }
        }
        return new AffectedOrderUnitsResult(new OrderUnit(unitName, currentQuantity, this.finalPrice), remaining);
    }

    static class AffectedOrderUnitsResult {
        private OrderUnit unit;
        private List<OrderUnit> remaining;

        public AffectedOrderUnitsResult(OrderUnit unit, List<OrderUnit> remaining) {
            this.unit = unit;
            this.remaining = remaining;
        }
    }
}

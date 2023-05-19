package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutSolution {

    /**
     * Class to represent the SKU and quantity being ordered.
     */
    static class OrderUnit {
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
            Integer offerTotal = 0;
            Integer offerAffected = 0;
            if (this.matchedOffer != null ) {
                offerTotal = this.matchedOffer != null? this.matchedOffer.finalPrice : 0;
                offerAffected = this.matchedOffer.quantity;
            }
            return offerTotal + ((quantity - offerAffected) * price);
        }

        public void setMatchedOffer(Offer matchedOffer) {
            this.matchedOffer = matchedOffer;
        }
    }

    /**
     * Represents the result of a check
     * It can internally represent OrderUnit that matched with an Offer
     * and its remaining items that didn't matched.
     */
    static class OfferRuleCheckResult {
        List<OrderUnit> matched = new ArrayList<>();
        List<OrderUnit> unmatched = new ArrayList<>();

        public void addMatched(OrderUnit unit){ this.matched.add(unit);}
        public void addUnMatched(OrderUnit unit){ this.matched.add(unit);}
    }

    static class OfferRule {
        private final String sku;
        private final Integer quantity;

        public OfferRule(String sku, Integer quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }

        public String getSku() {
            return sku;
        }

        /**
         * Method to identify if this specific condition is satisfied by the SKUs
         * Note: To be able to match a Condition twice, the Offer should be able to subtract the skus size.
         * This way we can apply the check recursively.
         * @param unit
         * @return
         */
        Boolean isSatisfiedBy(OrderUnit unit){
            return unit.quantity >= quantity;
        }
    }

    /**
     * A class to represent an Offer
     *
     * An Offer is composed by multiple OfferCondition,
     * this way is possible to create an Offer able to match a bundle of different SKUs.
     */
    static class Offer {

        private final String sku;
        private final OfferRule rule;
        private final Integer finalPrice;

        //Testing if is simple to directly set how many items are affected by this offer.
        private final Integer quantity;

        public Offer(String sku, OfferRule rule, Integer finalPrice, Integer quantity) {
            this.sku = sku;
            this.rule = rule;
            this.finalPrice = finalPrice;
            this.quantity = quantity;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Integer getFinalPrice() {
            return finalPrice;
        }

       public Boolean isSatisfiedBy(OrderUnit unit){
            return this.rule.isSatisfiedBy(unit);
       }

        //TODO: We''l probably need a method to compute the price (probably a command class to represent it)
        //This way, we can ensure the class will consider the items that matched with Offers and those units that didn't.
        public Integer apply() {
            return finalPrice;
        }
    }

    private final static List<Offer> offers = new ArrayList<>();
    private final static Map<String, Integer> prices = new HashMap<>();

    static {
        prices.put("A", 50);
        prices.put("B", 30);
        prices.put("C", 20);
        prices.put("D", 15);

        offers.add(new Offer("A", new OfferRule("A", 3), 130, 3));
        offers.add(new Offer("A", new OfferRule("B", 2), 45, 3));
    }

    public Integer checkout(String skus) {
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        Map<String, OrderUnit>  orderUnits = parseSKUs(skus);
        List<OrderUnit> processedOrder = processOrder(orderUnits.values());
        return processedOrder.stream().mapToInt(OrderUnit::getTotal).sum();
    }

    //Checks for matching Offers and return a list of OrderUnit with those that matched and those that didn't split in different instances
    public static List<OrderUnit> processOrder(Collection<OrderUnit> units) {

        return new ArrayList<>(units);
    }

    public static Map<String, OrderUnit> parseSKUs(String skus) {
        Map<String, List<String>> parsedSKus = Arrays
                                                .asList(skus.split(","))
                                                    .stream()
                                                    .collect(Collectors.groupingBy(item -> item));

        Map<String, OrderUnit> orderUnits = new HashMap<>();
        parsedSKus.entrySet().forEach(entry -> {
            orderUnits.put(entry.getKey(), new OrderUnit(entry.getKey(), entry.getValue().size(), prices.get(entry.getKey())));
        });
        return orderUnits;
    }

    private void assignOffers(Map<String, OrderUnit>  units){
        offers.forEach(offer -> {
            OrderUnit unit = units.get(offer.sku);
            if (offer.isSatisfiedBy(unit)) {
                unit.setMatchedOffer(offer);
            }
        });
    }
}









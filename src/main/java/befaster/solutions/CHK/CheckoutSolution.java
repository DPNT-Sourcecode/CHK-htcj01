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

        /**
         * Represents offers that matched with this OrderUnit
         */
        private final List<Offer> matchedOffers = new ArrayList<>();

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
            if (offers.size() > 0) return offers.stream().mapToInt(Offer::getFinalPrice).sum();
            return quantity * price;
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
        private final Map<String, OfferRule> rules = new HashMap<>();
        private final Integer finalPrice;

        public Offer(OfferRule rule, Integer finalPrice) {
            this(List.of(new OfferRule[]{rule}), finalPrice);
        }

        public Offer(List<OfferRule> offerRules, Integer finalPrice) {
            //Only allow 1 condition per SKU
            offerRules.forEach(rule -> {
                rules.putIfAbsent(rule.sku, rule);
            });
            this.finalPrice = finalPrice;
        }

        public Integer getFinalPrice() {
            return finalPrice;
        }

        /**
         * Iterates over the Offer conditions until identify there are no matching conditions.
         * If there are matching conditions, the final price is computed.
         * @param orderUnits
         * @return
         */
        OfferRuleCheckResult isSatisfiedBy(List<OrderUnit> orderUnits){
            //NOTE: This method shouldn't return a boolean
            //If there are matches this method should return a new List of OrderUnit.
            OfferRuleCheckResult result = new OfferRuleCheckResult();
            orderUnits.forEach(unit -> {
                OfferRule rule = rules.get(unit.sku);
                //The unit can match with a same offer multiple times.
                //How can we handle it?
                //We can create a class to represent how many times it matched.
                Boolean isSatisfied = rule.isSatisfiedBy(unit);
                if ( isSatisfied) {
                    result.addMatched(unit);
                } else {
                    result.addUnMatched(unit);
                }
            });
            return result;
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

        offers.add(new Offer(new OfferRule("A", 3), 130));
        offers.add(new Offer(new OfferRule("B", 2), 45));
    }

    public Integer checkout(String skus) {
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        Map<String, OrderUnit>  orderUnits = parseSKUs(skus);
        List<OrderUnit> processedOrder = processOrder(orderUnits.values());
        return processedOrder.stream().mapToInt(OrderUnit::getTotal).sum();
    }

    //Checks for matching Offers and return a list of OrderUnit with those that matched and those that didn't split in different instances
    public static List<OrderUnit> processOrder(Collection<OrderUnit> units) {
        return null;
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

    /**
     * Check for matches with the Offers
     * This method will:
     * - Check by matches
     * - Split the Order Units by those that matched with Offers and the remaining items
     * @param orderUnits
     * @return
     */
    private Integer checkMatchWithOffers(List<OrderUnit> orderUnits) {

        offers.forEach(offer -> {
            OfferRuleCheckResult result = offer.isSatisfiedBy(orderUnits);
        });
        return 0;
    }
}

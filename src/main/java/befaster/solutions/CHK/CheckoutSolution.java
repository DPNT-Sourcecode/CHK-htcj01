package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.Arrays;
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

        private final List<Offer> matchedOffers = new ArrayList<>();

        public OrderUnit(String sku, Integer quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }

        public String getSku() {
            return sku;
        }
        public Integer getTotal() {
            return 0;
        }
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
        boolean isSatisfiedBy(OrderUnit unit){
            //TODO:
            //What can happen if I have multiple items that can match with the same offer twice?
            //Do I have to implement a method to calculate the price?
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
        boolean isSatisfiedBy(List<OrderUnit> orderUnits){
            orderUnits.forEach(unit -> {
                OfferRule rule = rules.get(unit.sku);
                //The unit can match with a same offer multiple times.
                //How can we handle it?
                //We can create a class to represent how many times it matched.
                Boolean isSatisfied = rule.isSatisfiedBy(unit);

            });
            return false;
        }

        //TODO: We''l probably need a method to compute the price (probably a command class to represent it)
        //This way, we can ensure the class will consider the items that matched with Offers and those units that didn't.
        public Integer apply() {
            return finalPrice;
        }
    }

    private final static List<Offer> offers = new ArrayList<>();

    static {

    }

    public Integer checkout(String skus) {
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        List<String> parsedSKus = Arrays.asList(skus.split(","));

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
            Boolean isSatisfied = offer.isSatisfiedBy(orderUnits);
        });
    }
}





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
            Integer timesAffected = 0;
            Integer remainingQuantity = this.quantity;
            if (this.matchedOffer != null ) {
                offerTotal = this.matchedOffer != null? this.matchedOffer.finalPrice : 0;
                timesAffected = (this.quantity / this.matchedOffer.getQuantity());
                remainingQuantity = this.quantity % this.matchedOffer.getQuantity();
            }
            return ( offerTotal * timesAffected) + (remainingQuantity * price);
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

        public Offer(String sku, OfferRule rule, Integer finalPrice) {
            this.sku = sku;
            this.rule = rule;
            this.finalPrice = finalPrice;
        }

        public Integer getQuantity() {
            return rule.quantity;
        }

        public Integer getFinalPrice() {
            return finalPrice;
        }

       public Boolean isSatisfiedBy(OrderUnit unit){
            return this.rule.isSatisfiedBy(unit);
       }
    }

    private final static List<Offer> offers = new ArrayList<>();
    private final static Map<String, Integer> prices = new HashMap<>();

    static {
        prices.put("A", 50);
        prices.put("B", 30);
        prices.put("C", 20);
        prices.put("D", 15);

        offers.add(new Offer("A", new OfferRule("A", 3), 130));
        offers.add(new Offer("B", new OfferRule("B", 2), 45));
    }

    public Integer checkout(String skus) {
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        List<String> skusList = List.of(skus.split(",")) ;
        if (skusList.stream().filter(prices.keySet()::contains).count() <= skusList.size()) return -1;
        Map<String, OrderUnit>  orderUnits = parseSKUs(skusList);
        assignOffers(orderUnits);
        return orderUnits.values().stream().mapToInt(OrderUnit::getTotal).sum();
    }

    public static Map<String, OrderUnit> parseSKUs(List<String> skus) {
        Map<String, List<String>> parsedSKus = skus
                                                    .stream()
                                                    .collect(Collectors.groupingBy(item -> item));

        Map<String, OrderUnit> orderUnits = new HashMap<>();
        parsedSKus.entrySet().forEach(entry -> {
            orderUnits.put(entry.getKey(), new OrderUnit(entry.getKey(), entry.getValue().size(), prices.getOrDefault(entry.getKey(), 0)));
        });
        return orderUnits;
    }

    private void assignOffers(Map<String, OrderUnit>  units){
        offers.forEach(offer -> {
            OrderUnit unit = units.get(offer.sku);
            if (unit != null && offer.isSatisfiedBy(unit)) {
                unit.setMatchedOffer(offer);
            }
        });
    }
}







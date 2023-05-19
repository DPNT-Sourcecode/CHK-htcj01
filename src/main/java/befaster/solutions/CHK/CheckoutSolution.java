package befaster.solutions.CHK;

import befaster.runner.SolutionNotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
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

        public OrderUnit(String sku, Integer quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }

        public String getSku() {
            return sku;
        }
    }

    static class OfferCondition {
        private final String sku;
        private final Integer quantity;

        public OfferCondition(String sku, Integer quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }

        /**
         * Method to identify if this specific condition is satisfied by the SKUs
         * Note: To be able to match a Condition twice, the Offer should be able to subtract the skus size.
         * This way we can apply the check recursively.
         * @param skuIds
         * @return
         */
        boolean isSatisfiedBy(List<String> skuIds){
            Map<String, List<String>> map = skuIds.stream().collect(Collectors.groupingBy((item) -> item));
            //TODO:
            //What can happen if I have multiple items that can match with the same offer twice?
            //Do I have to implement a method to calculate the price?
            return map.getOrDefault(sku, new ArrayList<>()).size() >= quantity;
        }
    }

    /**
     * A class to represent an Offer
     *
     * An Offer is composed by multiple OfferCondition,
     * this way is possible to create an Offer able to match a bundle of different SKUs.
     */
    static class Offer {
        private final List<OfferCondition> offerConditions = new ArrayList<>();
        private final Integer finalPrice;

        public Offer(List<OfferCondition> offerConditions, Integer finalPrice) {
            this.offerConditions.addAll(offerConditions);
            this.finalPrice = finalPrice;
        }

        public Integer getFinalPrice() {
            return finalPrice;
        }

        /**
         * Iterates over the Offer conditions until identify there are no matching conditions.
         * If there are matching conditions, the final price is computed.
         * @param skuIds
         * @return
         */
        boolean isSatisfiedBy(List<OrderUnit> orderUnits){
            return false;
        }

        //TODO: We''l probably need a method to compute the price (probably a command class to represent it)
        //This way, we can ensure the class will consider the items that matched with Offers and those units that didn't.
    }

    private final static List<Offer> offers = new ArrayList<>();

    static {

    }

    public Integer checkout(String skus) {
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        List<String> parsedSKus = Arrays.asList(skus.split(","));

    }

    private Integer checkMatchWithOffers(List<OrderUnit> orderUnits) {
        offers.forEach(offer -> {
            Boolean isSatisfied = offer.isSatisfiedBy(skus);
        });
    }
}



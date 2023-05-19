package befaster.solutions.CHK;

import befaster.runner.SolutionNotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutSolution {

    static class OfferCondition {
        private final String sku;
        private final Integer quantity;

        public OfferCondition(String sku, Integer quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }

        boolean isSatisfiedBy(List<String> skuIds){
            Map<String, List<String>> map = skuIds.stream().collect(Collectors.groupingBy((item) -> item));
            //TODO:
            //What can happen if I have multiple items that can match with the same offer twice?
            //Do I have to implement a method to calculate the price?
            return map.getOrDefault(sku, new ArrayList<>()).size() >= quantity;
        }
    }

    static class Offer {
        private final List<String> expectedSkus;
        private final Integer finalPrice;

        public Offer(List<String> expectedSkus, Integer finalPrice) {
            this.expectedSkus = expectedSkus;
            this.finalPrice = finalPrice;
        }

        public Integer getFinalPrice() {
            return finalPrice;
        }

        boolean isSatisfiedBy(List<String> skuIds){
            return false;
        }
    }

    private final static List<Offer> offers = new ArrayList<>();

    static {
        Offer offer = new Offer();
        offers.add(offer);
    }

    public Integer checkout(String skus) {
        throw new SolutionNotImplementedException();
    }
}


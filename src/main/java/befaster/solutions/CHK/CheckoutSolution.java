package befaster.solutions.CHK;

import befaster.runner.SolutionNotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class CheckoutSolution {

    static class OfferCondition {
        private final String sku;
        private final Integer quantity;

        public OfferCondition(String sku, Integer quantity) {
            this.sku = sku;
            this.quantity = quantity;
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



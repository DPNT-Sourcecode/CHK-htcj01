package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderUnitTest {

    @Test
    void recursivelyComputeTotal() {
        OrderUnit unit = new OrderUnit("A", 8, 50);
        Offer firstOffer = new Offer("A", new BasicOfferRule("A", 5), 200, null);
        Offer secondOffer = new Offer("A", new BasicOfferRule("A", 3), 130, null);

        unit.setMatchedOffer(firstOffer);
        unit.setMatchedOffer(secondOffer);

        Integer total = unit.recursivelyComputeTotal(unit.getQuantity());
        Assertions.assertEquals(330, total);
    }

    @Test
    void recursivelyComputeTotalWithRemainingItem() {
        OrderUnit unit = new OrderUnit("A", 9, 50);
        Offer firstOffer = new Offer("A", new BasicOfferRule("A", 5), 200, null);
        Offer secondOffer = new Offer("A", new BasicOfferRule("A", 3), 130, null);

        unit.setMatchedOffer(firstOffer);
        unit.setMatchedOffer(secondOffer);

        Integer total = unit.recursivelyComputeTotal(unit.getQuantity());
        Assertions.assertEquals(380, total);
    }
}
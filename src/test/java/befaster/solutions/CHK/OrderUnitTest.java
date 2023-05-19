package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderUnitTest {

    @Test
    void recursivelyComputeTotal() {
        OrderUnit unit = new OrderUnit("A", 8, 50);
        Offer firstOffer = new Offer("A", new OfferRule("A", 5), 200, null);
        Offer secondOffer = new Offer("A", new OfferRule("A", 3), 130, null);

        unit.setMatchedOffer(firstOffer);
        unit.setMatchedOffer(secondOffer);

        Integer total = unit.recursivelyComputeTotal(unit.getQuantity());
        Assertions.assertEquals(330, total);
    }

    @Test
    void recursivelyComputeTotalWithRemainingItem() {
        OrderUnit unit = new OrderUnit("A", 9, 50);
        Offer firstOffer = new Offer("A", new OfferRule("A", 5), 200, null);
        Offer secondOffer = new Offer("A", new OfferRule("A", 3), 130, null);

        unit.setMatchedOffer(firstOffer);
        unit.setMatchedOffer(secondOffer);

        Integer total = unit.recursivelyComputeTotal(unit.getQuantity());
        Assertions.assertEquals(380, total);
    }
}
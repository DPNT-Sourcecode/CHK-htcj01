package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutSolutionTest {

    /**
     * Test List:
     *  - [checkout] A simple scenario with skus won't match any Offer
     *  - [checkout] A scenario where skus will match a single offer
     *  - [checkout] A scenario where skus will match multiple offers
     *  - [checkout] A scenario where the quantity of skus will match an Offer twice
     *  - [checkout] A scenario where the skus will match an offer twice with remaining items. (e.g size: 7 should match twice and have 1 item left)
     */

    @Test
    public void shouldSumSkusWithoutMatchAnyOffer() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("A,B,C,D");
        Assertions.assertEquals(115, total);
    }

    @Test
    public void shouldTransformSkusToOrderUnits() {
        Map<String, CheckoutSolution.OrderUnit> result = CheckoutSolution.parseSKUs("A");
        Assertions.assertEquals(result.size(), 1);
    }

}
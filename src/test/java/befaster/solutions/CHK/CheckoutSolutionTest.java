package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CheckoutSolutionTest {

    /**
     *  - {"method":"checkout","params":["AAAAAAAA"],"id":"CHK_R2_020"}, expected: 330, got: 350
     *  - {"method":"checkout","params":["AAAAAAAAA"],"id":"CHK_R2_021"}, expected: 380, got: 390
     *  - {"method":"checkout","params":["AAAAAEEBAAABB"],"id":"CHK_R2_040"}, expected: 455, got: 490
     *
     *   - {"method":"checkout","params":["ABCDEABCDE"],"id":"CHK_R2_038"}, expected: 280, got: 265
     *  - {"method":"checkout","params":["CCADDEEBBA"],"id":"CHK_R2_039"}, expected: 280, got: 265
     */

    /**
     * Test List:
     *
     * Part 1:
     *  - [checkout] A simple scenario with skus won't match any Offer
     *  - [checkout] A scenario where skus will match a single offer
     *  - [checkout] A scenario where skus will match multiple offers
     *  - [checkout] A scenario where the quantity of skus will match an Offer twice
     *  - [checkout] A scenario where the skus will match an offer twice with remaining items. (e.g size: 7 should match twice and have 1 item left)
     *
     * Part 2:
     * - [checkout] Should prioritize the most beneficial offer to the user (e.g AAAAA = 200)
     * - [checkout] Should give a discount in the remaining item
     */

    @Test
    public void shouldIgnoreUnmappedSKU() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("AABCDFG");
        Assertions.assertEquals(-1, total);
    }

    @Test
    public void shouldSumSkusWithoutMatchAnyOffer() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("AABCD");
        Assertions.assertEquals(165, total);
    }

    @Test
    public void shouldSumSkusMatchingSingleOffer() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("AAABCD");
        Assertions.assertEquals(195, total);
    }

    @Test
    public void shouldSumSkusMatchingAnOfferTwice() {
        CheckoutSolution checkout = new CheckoutSolution();

        Assertions.assertEquals(330, checkout.checkout("AAAAAAAA"));
        Assertions.assertEquals(380, checkout.checkout("AAAAAAAAA"));

        //AAAAA = 200, EE = 80 (B), AAA=130, BB=45
        Assertions.assertEquals(455, checkout.checkout("AAAAAEEBAAABB"));

        //AA=100 B(B)=30 CC=40 DD=30 EE=80
        //It's failing because we're applying the discount in the computed total
        //Should not apply offers if there are discounts?
        Assertions.assertEquals(280, checkout.checkout("ABCDEABCDE")); //265
        Assertions.assertEquals(280, checkout.checkout("CCADDEEBBA"));

        Assertions.assertEquals(400, checkout.checkout("AAAAAAAAAA"));
        Assertions.assertEquals(445, checkout.checkout("AAAAAAAAAABB"));
        Assertions.assertEquals(465, checkout.checkout("AAAAAAAAAABBC"));
    }

    @Test
    public void shouldSumSkusMatchingMultipleOffers() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("AAABBCD");
        Assertions.assertEquals(210, total);
    }

    @Test
    public void shouldTransformSkusToOrderUnits() {
        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("A")).size(), 1);
        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("A","A")).size(), 1);
        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("A","A","B")).size(), 2);

        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("A")).get("A").getQuantity(), 1);
        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("A","A")).get("A").getQuantity(), 2);

        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("A","A")).get("A").getTotal(), 100);
        Assertions.assertEquals(CheckoutSolution.parseSKUs(List.of("B")).get("B").getTotal(), 30);
    }

    @Test
    public void shouldPreferBestOffer() {
        OrderUnit unit = new OrderUnit("A", 6, 50);
        Assertions.assertEquals(unit.getTotal(), 300);

        unit.setMatchedOffer(new Offer("A", new OfferRule("A", 3), 130, null));
        Assertions.assertEquals(unit.getTotal(), 260);

        unit.setMatchedOffer(new Offer("A", new OfferRule("A", 5), 200, null));
        Assertions.assertEquals(unit.getTotal(), 250);
    }

    @Test
    public void shouldApplyDiscountInTheNextItem() {
        CheckoutSolution checkout = new CheckoutSolution();
        Assertions.assertEquals(80, checkout.checkout("EEB"));
        Assertions.assertEquals(80, checkout.checkout("EE"));
        Assertions.assertEquals(160, checkout.checkout("EEBEEB"));
        Assertions.assertEquals(280, checkout.checkout("EEEEEEE"));
//        //EE EE EE E B
        Assertions.assertEquals(280, checkout.checkout("EEEEEEEB"));
        Assertions.assertEquals(20, checkout.checkout("FFF"));
    }
}

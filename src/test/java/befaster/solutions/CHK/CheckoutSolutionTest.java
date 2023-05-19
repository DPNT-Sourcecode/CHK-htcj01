package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CheckoutSolutionTest {

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
        Integer total = checkout.checkout("AABCDF");
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
        Assertions.assertEquals(260, checkout.checkout("AAAAAA"));
        Assertions.assertEquals(305, checkout.checkout("AAAAAABB"));
        Assertions.assertEquals(325, checkout.checkout("AAAAAABBC"));
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

        unit.setMatchedOffer(new Offer("A", new OfferRule("A", 3), 130));
        Assertions.assertEquals(unit.getTotal(), 260);

        unit.setMatchedOffer(new Offer("A", new OfferRule("A", 5), 200));
        Assertions.assertEquals(unit.getTotal(), 250);
    }

    @Test
    public void shouldApplyDiscountInTheNextItem() {

    }

}
package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    public void shouldIgnoreUnmappedSKU() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("A,A,B,C,D,F");
        Assertions.assertEquals(-1, total);
    }

    @Test
    public void shouldSumSkusWithoutMatchAnyOffer() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("A,A,B,C,D");
        Assertions.assertEquals(165, total);
    }

    @Test
    public void shouldSumSkusMatchingSingleOffer() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("A,A,A,B,C,D");
        Assertions.assertEquals(195, total);
    }

    @Test
    public void shouldSumSkusMatchingAnOfferTwice() {
        CheckoutSolution checkout = new CheckoutSolution();
        Assertions.assertEquals(260, checkout.checkout("A,A,A,A,A,A"));
        Assertions.assertEquals(305, checkout.checkout("A,A,A,A,A,A,B,B"));
        Assertions.assertEquals(325, checkout.checkout("A,A,A,A,A,A,B,B,C"));
    }

    @Test
    public void shouldSumSkusMatchingMultipleOffers() {
        CheckoutSolution checkout = new CheckoutSolution();
        Integer total = checkout.checkout("A,A,A,B,B,C,D");
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

}





package befaster.solutions.CHK;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CheckoutSolutionTest {

    /**
     *  - {"method":"checkout","params":["AAAAAAAA"],"id":"CHK_R2_020"}, expected: 330, got: 350
     *  - {"method":"checkout","params":["AAAAAAAAA"],"id":"CHK_R2_021"}, expected: 380, got: 390
     *  - {"method":"checkout","params":["AAAAAEEBAAABB"],"id":"CHK_R2_040"}, expected: 455, got: 490
     *
     *   - {"method":"checkout","params":["ABCDEABCDE"],"id":"CHK_R2_038"}, expected: 280, got: 265
     *  - {"method":"checkout","params":["CCADDEEBBA"],"id":"CHK_R2_039"}, expected: 280, got: 265
     *
     *   - {"method":"checkout","params":["FFFF"],"id":"CHK_R3_041"}, expected: 30, got: 50
     *  - {"method":"checkout","params":["FFABCDECBAABCABBAAAEEAAFF"],"id":"CHK_R3_001"}, expected: 695, got: 715
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
        Integer total = checkout.checkout("AABCDF0");
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

        unit.setMatchedOffer(new Offer("A", new BasicOfferRule("A", 3), 130, null));
        Assertions.assertEquals(unit.getTotal(), 260);

        unit.setMatchedOffer(new Offer("A", new BasicOfferRule("A", 5), 200, null));
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
        Assertions.assertEquals(30, checkout.checkout("FFFF"));
        Assertions.assertEquals(695, checkout.checkout("FFABCDECBAABCABBAAAEEAAFF"));
    }

    /**
     - {"method":"checkout","params":["UUU"],"id":"CHK_R4_054"}, expected: 120, got: 80
     - {"method":"checkout","params":["UUUUU"],"id":"CHK_R4_056"}, expected: 160, got: 200
     - {"method":"checkout","params":["UUUUUUUU"],"id":"CHK_R4_057"}, expected: 240, got: 280

     - {"method":"checkout","params":["NNN"],"id":"CHK_R4_105"}, expected: 120, got: 40
     - {"method":"checkout","params":["NNNM"],"id":"CHK_R4_106"}, expected: 120, got: 40
     - {"method":"checkout","params":["NNNNM"],"id":"CHK_R4_107"}, expected: 160, got: 80
     */
    @Test
    public void shouldApplyOffers() {
        CheckoutSolution checkout = new CheckoutSolution();
        Assertions.assertEquals(45, checkout.checkout("HHHHH"));
        Assertions.assertEquals(80, checkout.checkout("HHHHHHHHHH"));
        Assertions.assertEquals(125, checkout.checkout("HHHHHHHHHHHHHHH"));
        Assertions.assertEquals(135, checkout.checkout("HHHHHHHHHHHHHHHH"));

        Assertions.assertEquals(15, checkout.checkout("M"));
        Assertions.assertEquals(120, checkout.checkout("NNNM"));

        //UUU = 120
        Assertions.assertEquals(120, checkout.checkout("UUU"));
        //UUU (U) = 120 + U = 40 = 160
        Assertions.assertEquals(160, checkout.checkout("UUUUU"));

        //UUU (U) = 120 + UUU (U) = 120 = 240
        Assertions.assertEquals(240, checkout.checkout("UUUUUUUU"));


        Assertions.assertEquals(120, checkout.checkout("NNN"));
        Assertions.assertEquals(120, checkout.checkout("NNNM"));
        Assertions.assertEquals(160, checkout.checkout("NNNNM"));
    }

    @Test
    public void shouldAssignOffers() {
        CheckoutSolution solution = new CheckoutSolution();
        Map<String, OrderUnit> units = new HashMap<>();

        units.put("S", new OrderUnit("S", 1, 20));
        units.put("T", new OrderUnit("T", 1, 20));
        units.put("X", new OrderUnit("X", 1, 17));

        solution.assignOffers(units);
    }


    /*
        If we have the following order SSTX we should end up having the following units:
        S = 20
        STX = 45

        Steps:
            - S(2), T(1), X(1)
            - S(1) STX(3)

    */
    @Test
    public void shouldApplyBundleOffers() {
        /*
         - {"method":"checkout","params":["K"],"id":"CHK_R5_013"}, expected: 70, got: 80
         - {"method":"checkout","params":["ABCDEFGHIJKLMNOPQRSTUVW"],"id":"CHK_R5_033"}, expected: 795, got: 805
         - {"method":"checkout","params":["K"],"id":"CHK_R5_095"}, expected: 70, got: 80

         - {"method":"checkout","params":["KK"],"id":"CHK_R5_096"}, expected: 120, got: 150
         - {"method":"checkout","params":["KKK"],"id":"CHK_R5_097"}, expected: 190, got: 220
         - {"method":"checkout","params":["KKKK"],"id":"CHK_R5_098"}, expected: 240, got: 300

         - {"method":"checkout","params":["STXSTX"],"id":"CHK_R5_140"}, expected: 90, got: 124
         - {"method":"checkout","params":["SSSZ"],"id":"CHK_R5_142"}, expected: 65, got: 66
         - {"method":"checkout","params":["ZZZS"],"id":"CHK_R5_144"}, expected: 65, got: 90
         */
        CheckoutSolution checkout = new CheckoutSolution();
//        Assertions.assertEquals(45, checkout.checkout("STX"));

        //TODO: If there is a problem with the discount I can sort the items by the lowest price.
//        Assertions.assertEquals(45, checkout.checkout("XXX"));
//        Assertions.assertEquals(45, checkout.checkout("XST"));
//        Assertions.assertEquals(45, checkout.checkout("XSS"));
//        Assertions.assertEquals(62, checkout.checkout("XSST"));
//        Assertions.assertEquals(62, checkout.checkout("SSTX"));
//        Assertions.assertEquals(112, checkout.checkout("SSTXA"));
//        Assertions.assertEquals(112, checkout.checkout("ASSTX"));
//        Assertions.assertEquals(175, checkout.checkout("SSTAAA"));
//
//        Assertions.assertEquals(120, checkout.checkout("KK"));
//        Assertions.assertEquals(190, checkout.checkout("KKK"));
//        Assertions.assertEquals(240, checkout.checkout("KKKK"));

        //Last
        Assertions.assertEquals(90, checkout.checkout("STXSTX")); //Got: 124
//        Assertions.assertEquals(65, checkout.checkout("SSSZ")); //Got: 66
//        Assertions.assertEquals(65, checkout.checkout("ZZZS")); //Got 90
    }
}
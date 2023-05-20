package befaster.solutions.CHK;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void extractSimpleBundle() {
        Offer offer = new Offer("STXYZ", new GroupOfferRule(3, "S", "T", "X", "Y", "Z"),  45, null);

        Map<String, OrderUnit> unitMap = CheckoutSolution.parseSKUs(List.of("S", "T", "X"));
        OfferBundleResult result = offer.extractBundle(unitMap.values().stream().toList());

        List<OrderUnit> units = result.getUnits();
        assertEquals(units.size(), 1);
        assertEquals(units.stream().findFirst().get().getSku(), "STX");
        assertEquals(units.stream().findFirst().get().getPrice(), 45);
    }

    @Test
    void extractMoreComplexBundle() {
        Offer offer = new Offer("STXYZ", new GroupOfferRule(3, "S", "T", "X", "Y", "Z"),  45, null);

        Map<String, OrderUnit> unitMap = CheckoutSolution.parseSKUs(List.of("S","S", "T", "X"));
        OfferBundleResult result = offer.extractBundle(unitMap.values().stream().toList());

        List<OrderUnit> units = result.getUnits();
        assertEquals(units.size(), 2);
        assertEquals(units.get(0).getSku(), "ST");
        assertEquals(units.get(0).getPrice(), 45);

        assertEquals(units.get(1).getSku(), "X");
        assertEquals(units.get(1).getPrice(), 17);
    }

    @Test
    void extractMoreComplexBundleWithUnmatchedItems() {
        Offer offer = new Offer("STXYZ", new GroupOfferRule(3, "S", "T", "X", "Y", "Z"),  45, null);

        Map<String, OrderUnit> unitMap = CheckoutSolution.parseSKUs(List.of("S","S", "T", "X", "A"));
        OfferBundleResult result = offer.extractBundle(unitMap.values().stream().toList());

        List<OrderUnit> units = result.getUnits();
        assertEquals(units.size(), 2);
        assertEquals(units.get(0).getSku(), "ST");
        assertEquals(units.get(0).getPrice(), 45);

        assertEquals(units.get(1).getSku(), "X");
        assertEquals(units.get(1).getPrice(), 17);
    }
}
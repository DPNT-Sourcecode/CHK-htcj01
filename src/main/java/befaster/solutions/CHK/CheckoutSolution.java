package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CheckoutSolution {

    private final static List<Offer> offers = new ArrayList<>();
    private final static Map<String, Integer> prices = new HashMap<>();

    static {
        prices.put("A", 50);
        prices.put("B", 30);
        prices.put("C", 20);
        prices.put("D", 15);
        prices.put("E", 40);

        offers.add(new Offer("A", new OfferRule("A", 3), 130));

        offers.add(new Offer("A", new OfferRule("A", 5), 200));
        offers.add(new Offer("B", new OfferRule("B", 2), 45));

        //This won't work. It should have the B free
        Function<OfferContext, Integer> discountFN = (ctx) -> {
            Offer offer = ctx.offer();
            OrderUnit unit = ctx.unit();
            int remaining = unit.getQuantity() % offer.getQuantity();
            int timesToApply = unit.getQuantity() / offer.getQuantity();
            int total = unit.getQuantity() * unit.getPrice();
            int discount = timesToApply * unit.getPrice();
            return total - discount + (remaining * unit.getPrice());
        };
        offers.add(new Offer("E", new OfferRule("B", 3), discountFN));
    }

    public Integer checkout(String skus) {
        if ("".equalsIgnoreCase(skus)) return 0;
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        List<String> skusList = List.of(skus.split(""));
        if (!isAllSkuValid(skusList)) return -1;
        Map<String, OrderUnit>  orderUnits = parseSKUs(skusList);
        assignOffers(orderUnits);
        return orderUnits.values().stream().mapToInt(OrderUnit::getTotal).sum();
    }

    private boolean isAllSkuValid(List<String> skusList) {
        Set<String> keys = prices.keySet();
        boolean isValid = true;
        for (String sku: skusList) {
            if (!keys.contains(sku)){
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    public static Map<String, OrderUnit> parseSKUs(List<String> skus) {
        Map<String, List<String>> parsedSKus = skus
                                                    .stream()
                                                    .collect(Collectors.groupingBy(item -> item));

        Map<String, OrderUnit> orderUnits = new HashMap<>();
        parsedSKus.entrySet().forEach(entry -> {
            orderUnits.put(entry.getKey(), new OrderUnit(entry.getKey(), entry.getValue().size(), prices.getOrDefault(entry.getKey(), 0)));
        });
        return orderUnits;
    }

    private void assignOffers(Map<String, OrderUnit>  units){
        offers.forEach(offer -> {
            OrderUnit unit = units.get(offer.getSku());
            if (unit != null && offer.isSatisfiedBy(unit)) {
                unit.setMatchedOffer(offer);
            }
        });
    }
}



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

    public static final Function<OfferContext, Integer> BUY_SOME_GET_X_FREE = (ctx) -> {
        Offer offer = ctx.offer();
        OrderUnit unit = ctx.unit();
        int remaining = unit.getQuantity() % offer.getQuantity() - 1;
        if (remaining < 0) remaining = 0;
        int timesToApply = unit.getQuantity() / offer.getQuantity();
        int total = unit.getQuantity() * unit.getPrice();
        int discount = timesToApply * unit.getPrice();
        return total - discount + (remaining * unit.getPrice());
    };

    public static final Function<OfferContext, List<Discount>> DISCOUNT_TO_ANOTHER_SKU_FN = (ctx) -> {
        Offer offer = ctx.offer();
        OrderUnit unit = ctx.unit();
        int timesToApply = unit.getQuantity() / offer.getQuantity();
        List<Discount> discounts = new ArrayList<>();
        for (int i = 0; i < timesToApply; i++) {
            discounts.add(new Discount(prices.get("B") * -1, "B"));
        }
        return discounts;
    };

    public static Function<OfferContext, List<Discount>> createDiscountToSku(String SKU) {
        return (ctx) -> {
            Offer offer = ctx.offer();
            OrderUnit unit = ctx.unit();
            int timesToApply = unit.getQuantity() / offer.getQuantity();
            List<Discount> discounts = new ArrayList<>();
            for (int i = 0; i < timesToApply; i++) {
                discounts.add(new Discount(prices.get(SKU) * -1, SKU));
            }
            return discounts;
        };
    }

    static {
        prices.put("A", 50); prices.put("B", 30); prices.put("C", 20);
        prices.put("D", 15); prices.put("E", 40); prices.put("F", 10);
        prices.put("F", 10); prices.put("G", 20); prices.put("H", 10);
        prices.put("I", 35); prices.put("J", 60); prices.put("K", 80);
        prices.put("L", 90); prices.put("M", 15); prices.put("N", 40);
        prices.put("O", 10); prices.put("P", 50); prices.put("Q", 30);
        prices.put("R", 50); prices.put("S", 30); prices.put("T", 20);
        prices.put("U", 40); prices.put("V", 50); prices.put("W", 20);
        prices.put("X", 90); prices.put("Y", 10); prices.put("Z", 50);

        //Common Offers
        offers.add(new Offer("A", new BasicOfferRule("A", 3), 130, null));
        offers.add(new Offer("A", new BasicOfferRule("A", 5), 200, null));
        offers.add(new Offer("B", new BasicOfferRule("B", 2), 45, null));
        offers.add(new Offer("E", new BasicOfferRule("E", 2), 80, createDiscountToSku("B")));
        offers.add(new Offer("F", new BasicOfferRule("F", 3), BUY_SOME_GET_X_FREE, null));
        offers.add(new Offer("H", new BasicOfferRule("H", 5), 45, null));
        offers.add(new Offer("H", new BasicOfferRule("H", 10), 80, null));
        offers.add(new Offer("K", new BasicOfferRule("K", 2), 150, null));
        offers.add(new Offer("N", new BasicOfferRule("N", 3), 120, createDiscountToSku("M")));
        offers.add(new Offer("P", new BasicOfferRule("P", 5), 200, null));
        offers.add(new Offer("Q", new BasicOfferRule("Q", 3), 80, null));
        offers.add(new Offer("R", new BasicOfferRule("R", 3), 150, createDiscountToSku("Q")));
        offers.add(new Offer("U", new BasicOfferRule("U", 4), BUY_SOME_GET_X_FREE, null));
        offers.add(new Offer("V", new BasicOfferRule("V", 2),  90, null));
        offers.add(new Offer("V", new BasicOfferRule("V", 3),  130, null));

        //Group Discount Offer
        offers.add(new Offer("STXYZ", new GroupOfferRule(3, "S", "T", "X", "Y", "Z"),  45, null));
    }

    public Integer checkout(String skus) {
        if ("".equalsIgnoreCase(skus)) return 0;
        if (skus == null) throw new IllegalArgumentException("Skus can't be null");
        List<String> skusList = List.of(skus.split(""));
        if (!isAllSkuValid(skusList)) return -1;
        Map<String, OrderUnit>  orderUnits = parseSKUs(skusList);
        Map<String, OrderUnit> processedUnits = assignOffers(orderUnits);
        return processedUnits.values().stream().mapToInt(OrderUnit::getTotal).sum();
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

    public Map<String, OrderUnit> assignOffers(Map<String, OrderUnit>  units){
        List<Discount> discounts = new ArrayList<>();
        Map<String, OrderUnit> result = units;
        for (Offer offer : offers) {
            if (offer.isGroupOffer()){
//                OfferBundleResult bundle = offer.extractBundle(new ArrayList<>(units.values()));
//                List<OrderUnit> bundleUnits = bundle.getUnits();
//                //Note: We can internally change each OrderUnit quantity, but the best is to let it Immutable.
//                //So, we should return a new Map instead of change it.
//                result = bundleUnits.stream().collect(Collectors.toMap(OrderUnit::getSku, (item) -> item));
            } else {
                OrderUnit unit = units.get(offer.getSku());
                if (unit != null && offer.isSatisfiedBy(unit)) {
                    unit.setMatchedOffer(offer);
                    discounts.addAll(offer.computeDiscounts(new OfferContext(unit, offer)));
                }
            }
        }

        discounts.forEach(discount -> {
            OrderUnit unit = units.get(discount.getSku());
            if (unit != null) unit.addDiscount(discount);
        });
        return result;
    }
}



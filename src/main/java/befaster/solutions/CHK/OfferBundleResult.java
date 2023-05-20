package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

public class OfferBundleResult {

    private final List<Discount> discounts = new ArrayList<>();
    private final List<OrderUnit> units = new ArrayList<>();

    public void add(OrderUnit unit){
        this.units.add(unit);
    }

    public void add(Discount discount) {
        this.discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public List<OrderUnit> getUnits() {
        return units;
    }

    public void merge(OfferBundleResult other){
        this.discounts.addAll(other.discounts);
        this.units.addAll(other.units);
    }
}

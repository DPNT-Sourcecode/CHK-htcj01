package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

public class Order{

    private final List<OrderUnit> units = new ArrayList<>();

    public Order(List<OrderUnit> units) {
        this.units.addAll(units);
    }

    public List<OrderUnit> getUnits() {
        return units;
    }
}

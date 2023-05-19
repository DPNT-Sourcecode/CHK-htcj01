package befaster.solutions.CHK;

/**
 * Class to represent a discount to be applied
 */
public class Discount {

    private final Integer value;
    private final String sku;

    public Discount(Integer value, String sku) {
        this.value = value;
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public Integer getValue() {
        return value;
    }
}

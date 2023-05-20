package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

public class RuleCheckResult {

    private final boolean isSatisfied;
    private final List<OrderUnit> matched = new ArrayList<>();
    private final List<OrderUnit> unmatched = new ArrayList<>();

    public RuleCheckResult(boolean isSatisfied, List<OrderUnit> matched, List<OrderUnit> unmatched) {
        this.isSatisfied = isSatisfied;
        this.matched.addAll(matched);
        this.unmatched.addAll(unmatched);
    }

    public boolean isSatisfied() {
        return isSatisfied;
    }

    public List<OrderUnit> getMatched() {
        return matched;
    }

    public List<OrderUnit> getUnmatched() {
        return unmatched;
    }
}




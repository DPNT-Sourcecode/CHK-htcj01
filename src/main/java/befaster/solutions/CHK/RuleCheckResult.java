package befaster.solutions.CHK;

import java.util.ArrayList;
import java.util.List;

public class RuleCheckResult {

    private final List<OrderUnit> matched = new ArrayList<>();
    private final List<OrderUnit> unmatched = new ArrayList<>();

    public RuleCheckResult(List<OrderUnit> matched, List<OrderUnit> unmatched) {
        this.matched.addAll(matched);
        this.unmatched.addAll(unmatched);
    }

    public boolean isSatisfied() {
        return this.matched.size() > 0;
    }

    public List<OrderUnit> getMatched() {
        return matched;
    }

    public List<OrderUnit> getUnmatched() {
        return unmatched;
    }
}



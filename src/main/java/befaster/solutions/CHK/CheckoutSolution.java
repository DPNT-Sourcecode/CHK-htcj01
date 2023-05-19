package befaster.solutions.CHK;

import befaster.runner.SolutionNotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class CheckoutSolution {

    static class Offer {

    }

    private final static List<Offer> offers = new ArrayList<>();

    static {
        Offer offer = new Offer();
        offers.add(offer);
    }

    public Integer checkout(String skus) {
        throw new SolutionNotImplementedException();
    }
}


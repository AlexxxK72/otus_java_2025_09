package strategy;

import java.util.Map;
import model.Denomination;

public interface WithdrawStrategy {
    Map<Denomination, Integer> calculate(int amount, Map<Denomination, Integer> available);
}

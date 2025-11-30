package strategy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import model.Denomination;

public class MinimalBanknotesWithdrawStrategy implements WithdrawStrategy {
    @Override
    public Map<Denomination, Integer> calculate(int amount, Map<Denomination, Integer> available) {

        Map<Denomination, Integer> result = new EnumMap<>(Denomination.class);
        int remaining = amount;

        List<Denomination> orderedDesc = new ArrayList<>(available.keySet());
        orderedDesc.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        for (Denomination d : orderedDesc) {
            int value = d.getValue();
            int availableCount = available.get(d);
            int need = Math.min(remaining / value, availableCount);

            if (need > 0) {
                result.put(d, need);
                remaining -= need * value;
            }
        }

        if (remaining != 0) {
            return null;
        }

        return result;
    }
}

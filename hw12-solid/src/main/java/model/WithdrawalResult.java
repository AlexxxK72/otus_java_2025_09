package model;

import java.util.Map;

public class WithdrawalResult {

    private final Map<Denomination, Integer> banknotes;
    private final int totalAmount;

    public WithdrawalResult(Map<Denomination, Integer> banknotes) {
        this.banknotes = Map.copyOf(banknotes);
        this.totalAmount = calculateTotal(banknotes);
    }

    public Map<Denomination, Integer> getBanknotes() {
        return banknotes;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    private int calculateTotal(Map<Denomination, Integer> map) {
        return map.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }

    @Override
    public String toString() {
        return "WithdrawalResult{" + "banknotes=" + banknotes + ", totalAmount=" + totalAmount + '}';
    }
}

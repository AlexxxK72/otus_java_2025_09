package service;

import java.util.EnumMap;
import java.util.Map;
import model.Denomination;

public class CashDispenser {
    private final Map<Denomination, Integer> cells = new EnumMap<>(Denomination.class);

    public CashDispenser(Map<Denomination, Integer> cellsInitial) {
        cells.putAll(cellsInitial);
    }

    public void deposit(Map<Denomination, Integer> banknotes) {
        banknotes.forEach((denomination, count) -> cells.merge(denomination, count, Integer::sum));
    }

    public Map<Denomination, Integer> getAvailableNotes() {
        return new EnumMap<>(cells);
    }

    public void tryWithdraw(Map<Denomination, Integer> toWithdraw) {
        // Проверка
        toWithdraw.forEach((d, cnt) -> {
            if (cells.get(d) < cnt) {
                throw new IllegalArgumentException("Not enough banknotes: " + d);
            }
        });

        // Списание
        toWithdraw.forEach((d, cnt) -> cells.put(d, cells.get(d) - cnt));
    }

    public int getBalance() {
        return cells.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }
}

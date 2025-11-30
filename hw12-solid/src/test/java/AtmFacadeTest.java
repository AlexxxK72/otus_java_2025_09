import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;
import java.util.Map;
import model.Denomination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CashDispenser;
import strategy.MinimalBanknotesWithdrawStrategy;
import strategy.WithdrawStrategy;

class AtmFacadeTest {
    private AtmFacade atm;

    @BeforeEach
    void setup() {
        var dispenser = new CashDispenser();
        WithdrawStrategy strategy = new MinimalBanknotesWithdrawStrategy();
        atm = new AtmFacade(dispenser, strategy);
    }

    @Test
    void testDepositIncreasesBalance() {
        Map<Denomination, Integer> deposit = new EnumMap<>(Denomination.class);
        deposit.put(Denomination.HUNDRED, 5); // 500
        deposit.put(Denomination.FIFTY, 2); // 100

        atm.deposit(deposit);

        assertEquals(600, atm.getBalance());
    }

    @Test
    void testWithdrawSuccess() {
        // Подготовка: кладём 3 x 100 = 300
        Map<Denomination, Integer> deposit = new EnumMap<>(Denomination.class);
        deposit.put(Denomination.HUNDRED, 3);
        atm.deposit(deposit);

        // Снимаем 200
        Map<Denomination, Integer> withdrawn = atm.withdraw(200);

        assertEquals(2, withdrawn.get(Denomination.HUNDRED)); // 2 x 100
        assertEquals(100, atm.getBalance()); // Остаток 100
    }

    @Test
    void testWithdrawFailsWhenImpossible() {
        // Кладём 150
        Map<Denomination, Integer> deposit = new EnumMap<>(Denomination.class);
        deposit.put(Denomination.HUNDRED, 1);
        deposit.put(Denomination.FIFTY, 1);
        atm.deposit(deposit);

        // Пытаемся снять 120 — невозможно
        assertThrows(IllegalArgumentException.class, () -> atm.withdraw(120));
    }

    @Test
    void testWithdrawExact() {
        // Кладём 1 х 50, 1 х 100
        Map<Denomination, Integer> deposit = new EnumMap<>(Denomination.class);
        deposit.put(Denomination.FIFTY, 1);
        deposit.put(Denomination.HUNDRED, 1);
        atm.deposit(deposit);

        // Снимаем ровно 150
        Map<Denomination, Integer> withdrawn = atm.withdraw(150);

        assertEquals(1, withdrawn.get(Denomination.FIFTY));
        assertEquals(1, withdrawn.get(Denomination.HUNDRED));
        assertEquals(0, atm.getBalance());
    }
}

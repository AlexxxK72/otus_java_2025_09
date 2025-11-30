import java.util.Map;
import model.Denomination;
import service.CashDispenser;
import strategy.WithdrawStrategy;

public class AtmFacade {

    private final CashDispenser dispenser;
    private final WithdrawStrategy withdrawStrategy;

    public AtmFacade(CashDispenser cashDispenser, WithdrawStrategy withdrawStrategy) {
        this.dispenser = cashDispenser;
        this.withdrawStrategy = withdrawStrategy;
    }

    public void deposit(Map<Denomination, Integer> banknotes) {
        dispenser.deposit(banknotes);
    }

    public Map<Denomination, Integer> withdraw(int amount) {
        Map<Denomination, Integer> available = dispenser.getAvailableNotes();
        Map<Denomination, Integer> plan = withdrawStrategy.calculate(amount, available);

        if (plan == null) {
            throw new IllegalArgumentException("Cannot withdraw: " + amount);
        }

        dispenser.tryWithdraw(plan);
        return plan;
    }

    public int getBalance() {
        return dispenser.getBalance();
    }
}

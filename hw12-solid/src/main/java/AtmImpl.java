import java.util.Map;
import model.Denomination;
import model.WithdrawalResult;
import service.CashDispenser;
import strategy.WithdrawStrategy;

public class AtmImpl implements Atm {

    private final CashDispenser dispenser;
    private final WithdrawStrategy withdrawStrategy;

    public AtmImpl(CashDispenser cashDispenser, WithdrawStrategy withdrawStrategy) {
        this.dispenser = cashDispenser;
        this.withdrawStrategy = withdrawStrategy;
    }

    @Override
    public void deposit(Map<Denomination, Integer> banknotes) {
        dispenser.deposit(banknotes);
    }

    @Override
    public WithdrawalResult withdraw(int amount) {
        Map<Denomination, Integer> available = dispenser.getAvailableNotes();
        Map<Denomination, Integer> plan = withdrawStrategy.calculate(amount, available);

        if (plan.isEmpty()) {
            throw new IllegalArgumentException("Cannot withdraw: " + amount);
        }

        dispenser.tryWithdraw(plan);
        return new WithdrawalResult(plan);
    }

    @Override
    public int getBalance() {
        return dispenser.getBalance();
    }
}

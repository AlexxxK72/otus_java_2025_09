import java.util.Map;
import model.Denomination;
import model.WithdrawalResult;

public interface Atm {

    void deposit(Map<Denomination, Integer> banknotes);

    WithdrawalResult withdraw(int amount);

    int getBalance();
}

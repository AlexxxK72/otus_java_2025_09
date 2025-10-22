package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private final Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.add(copy(customer));
    }

    public Customer take() {
        return customers.pollLast();
    }

    private Customer copy(Customer original) {
        return new Customer(original.getId(), original.getName(), original.getScores());
    }
}

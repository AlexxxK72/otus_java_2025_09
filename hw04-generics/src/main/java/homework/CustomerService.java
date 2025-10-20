package homework;

import java.util.*;

public class CustomerService {

    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map. Entry сделан в jdk
        var firstEntry = customers.firstEntry();
        return firstEntry == null ? null : Map.entry(copy(firstEntry.getKey()), firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var nextEntry = customers.higherEntry(customer);
        return nextEntry == null ? null : Map.entry(copy(nextEntry.getKey()), nextEntry.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(copy(customer), data);
    }

    private Customer copy(Customer original) {
        return new Customer(original.getId(), original.getName(), original.getScores());
    }
}

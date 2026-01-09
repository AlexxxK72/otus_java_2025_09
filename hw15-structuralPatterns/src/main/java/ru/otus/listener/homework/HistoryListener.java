package ru.otus.listener.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        ObjectForMessage field13Copy = null;
        if (msg.getField13() != null) {
            field13Copy = new ObjectForMessage();
            if (msg.getField13().getData() != null) {
                field13Copy.setData(new ArrayList<>(msg.getField13().getData()));
            }
        }

        Message copy = msg.toBuilder().field13(field13Copy).build();

        history.put(msg.getId(), copy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}

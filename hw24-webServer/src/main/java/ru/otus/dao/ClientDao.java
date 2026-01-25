package ru.otus.dao;

import java.util.List;
import ru.otus.crm.model.Client;

public interface ClientDao {

    List<Client> findAll();

    void add(Client client);
}

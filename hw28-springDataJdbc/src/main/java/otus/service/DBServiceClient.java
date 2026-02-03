package otus.service;

import java.util.List;
import otus.model.Client;

public interface DBServiceClient {

    void save(Client client);

    List<Client> findAll();
}

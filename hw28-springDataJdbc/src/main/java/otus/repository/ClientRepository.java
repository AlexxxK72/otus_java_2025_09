package otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import otus.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {}

package otus.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import otus.crm.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {}

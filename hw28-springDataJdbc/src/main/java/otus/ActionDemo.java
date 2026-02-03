package otus;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import otus.crm.model.Address;
import otus.crm.model.Client;
import otus.crm.model.Phone;
import otus.crm.repository.ClientRepository;
import otus.crm.service.DBServiceClient;

@Component("actionDemo")
public class ActionDemo implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);

    private final ClientRepository clientRepository;
    private final DBServiceClient dbServiceClient;

    public ActionDemo(ClientRepository clientRepository, DBServiceClient dbServiceClient) {
        this.clientRepository = clientRepository;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void run(String... args) {

        //// создаем Manager
        log.info(">>> manager creation");

        /// создаем Client
        var firstClient = dbServiceClient.saveClient(new Client(
                null, "firstClient", new Address(null, "Address1"), Set.of(new Phone(null, "89267290888", null))));

        var clientSecond = dbServiceClient.saveClient(new Client(
                null, "secondClient", new Address(null, "Address2"), Set.of(new Phone(null, "89035368218", null))));

        var clientSecondSelected = dbServiceClient.saveClient(new Client(
                null, "secondClient", new Address(null, "Address2"), Set.of(new Phone(null, "89035368218", null))));

        log.info(">>> clientSecondSelected:{}", clientSecondSelected);

        log.info("delete instead of update");
        /// обновляем Client
        dbServiceClient.saveClient(new Client(
                clientSecondSelected.getId(),
                "dbServiceSecondUpdated",
                new Address(null, "Address2"),
                Set.of(new Phone(null, "89035368218", null))));
        var clientUpdated = dbServiceClient
                .getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        /// получаем все сущности
        log.info(">>> All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}

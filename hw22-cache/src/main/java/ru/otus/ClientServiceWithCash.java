package ru.otus;

import java.util.List;
import java.util.Optional;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.jdbc.core.executor.DbExecutorImpl;
import ru.otus.jdbc.core.sessionmanager.TransactionRunner;
import ru.otus.jdbc.crm.model.Client;
import ru.otus.jdbc.crm.service.DBServiceClient;
import ru.otus.jdbc.crm.service.DataTemplateJdbc;
import ru.otus.jdbc.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;

public class ClientServiceWithCash implements DBServiceClient {

    private final DBServiceClient dbServiceClient;
    private final HwCache<Long, Client> cache;

    public ClientServiceWithCash(TransactionRunner transactionRunner) {
        dbServiceClient = new DbServiceClientImpl(
                transactionRunner,
                new DataTemplateJdbc<>(
                        new DbExecutorImpl(),
                        new EntitySQLMetaDataImpl<>(new EntityClassMetaDataImpl<>(Client.class))));
        cache = new MyCache<>();
    }

    @Override
    public Client saveClient(Client client) {
        var saved = dbServiceClient.saveClient(client);
        if (saved.getId() != null) {
            cache.put(saved.getId(), saved);
        }
        return saved;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var fromCache = cache.get(id);
        if (fromCache != null) {
            return Optional.of(fromCache);
        }
        var fromDb = dbServiceClient.getClient(id);
        fromDb.ifPresent(c -> cache.put(c.getId(), c));
        return fromDb;
    }

    @Override
    public List<Client> findAll() {
        var list = dbServiceClient.findAll();
        list.forEach(c -> {
            if (c.getId() != null) {
                cache.put(c.getId(), c);
            }
        });
        return list;
    }
}

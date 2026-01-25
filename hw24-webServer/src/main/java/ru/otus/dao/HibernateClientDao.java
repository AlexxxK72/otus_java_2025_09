package ru.otus.dao;

import java.util.List;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;

public class HibernateClientDao implements ClientDao {

    private final DBServiceClient dbServiceClient;
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public HibernateClientDao() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        this.dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }

    @Override
    public void add(Client client) {
        dbServiceClient.saveClient(client);
    }
}

package ru.otus;

import java.util.stream.Stream;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.jdbc.crm.datasource.DriverManagerDataSource;
import ru.otus.jdbc.crm.service.DBServiceClient;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";
    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    private static DBServiceClient serviceClient;

    public static void main(String[] args) {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        serviceClient = new ClientServiceWithCash(transactionRunner);

        findClientsWithoutCache();
        serviceClient.findAll();
        findClientsWithCache();
    }

    private static void findClientsWithoutCache() {
        var startTime = System.currentTimeMillis();
        Stream.iterate(1L, i -> i + 1)
                .limit(20)
                .forEach(id -> serviceClient.getClient(id).ifPresent(client -> log.info(client.toString())));
        var endTime = System.currentTimeMillis();
        log.info("Time without cache: {} ms", endTime - startTime);
    }

    private static void findClientsWithCache() {
        var startTime = System.currentTimeMillis();
        Stream.iterate(1L, i -> i + 1)
                .limit(20)
                .forEach(id -> serviceClient.getClient(id).ifPresent(client -> log.info(client.toString())));
        var endTime = System.currentTimeMillis();
        log.info("Time with cache: {} ms", endTime - startTime);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}

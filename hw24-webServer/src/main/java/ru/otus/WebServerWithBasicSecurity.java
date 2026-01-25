package ru.otus;

import java.net.URI;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.crm.service.TemplateProcessor;
import ru.otus.crm.service.TemplateProcessorImpl;
import ru.otus.dao.ClientDao;
import ru.otus.dao.HibernateClientDao;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithBasicSecurity;

public class WebServerWithBasicSecurity {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        ClientDao clientDao = new HibernateClientDao();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));

        LoginService loginService = new HashLoginService(REALM_NAME, configResource);

        UsersWebServer usersWebServer =
                new UsersWebServerWithBasicSecurity(clientDao, WEB_SERVER_PORT, loginService, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}

package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.TemplateProcessor;
import ru.otus.dao.ClientDao;

public class ClientServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final transient ClientDao clientDao;
    private final transient TemplateProcessor templateProcessor;

    public ClientServlet(ClientDao clientDao, TemplateProcessor templateProcessor) {
        this.clientDao = clientDao;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clientDao.findAll());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String street = req.getParameter("street");

        Address address = new Address(null, street);
        String[] phoneNumbers = req.getParameterValues("phone");
        List<Phone> phones =
                Optional.ofNullable(phoneNumbers).map(Arrays::asList).orElse(Collections.emptyList()).stream()
                        .filter(s -> s != null && !s.trim().isEmpty())
                        .map(num -> new Phone(null, num))
                        .toList();

        Client client = new Client(null, name, address, phones);
        clientDao.add(client);
        resp.sendRedirect("/clients");
    }
}

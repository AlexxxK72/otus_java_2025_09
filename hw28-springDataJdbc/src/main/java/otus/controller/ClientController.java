package otus.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.crm.model.Address;
import otus.crm.model.Client;
import otus.crm.model.Phone;
import otus.crm.service.DBServiceClient;
import otus.crm.service.TemplateProcessor;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    @GetMapping
    public String listClients() throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        Map<String, Object> data = Map.of("clients", clients);
        return templateProcessor.getPage("clients", data); // HTML!
    }

    @PostMapping
    public ResponseEntity<Client> createClient(
            @RequestParam String name, @RequestParam String street, @RequestParam(required = false) Set<String> phone) {
        return ResponseEntity.ok(
                dbServiceClient.saveClient(new Client(null, name, new Address(null, street), Set.of(new Phone()))));
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return dbServiceClient.getClient(id).orElse(null);
    }
}

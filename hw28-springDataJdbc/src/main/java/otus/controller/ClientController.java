package otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import otus.model.Client;
import otus.service.DBServiceClient;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final DBServiceClient dbServiceClient;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", dbServiceClient.findAll());
        return "clients";
    }

    @PostMapping
    public String createClient(Client client) {
        if (client.getPhones() != null) {
            client.setPhones(client.getPhones().stream()
                    .filter(phone -> StringUtils.hasText(phone.getNumber()))
                    .toList());
        }
        dbServiceClient.save(client);
        return "redirect:/clients";
    }
}

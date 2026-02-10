package otus.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import otus.model.Client;
import otus.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}

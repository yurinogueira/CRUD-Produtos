package br.net.yurinogueira.springsales.domain.service.impl;

import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.repository.ClientRepository;
import br.net.yurinogueira.springsales.domain.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    @Transactional
    public void save(Client client) {
        repository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Client get(Integer integer) {
        Optional<Client> client = repository.findById(integer);
        if (client.isEmpty()) {
            return null;
        }
        return client.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> search(Example<Client> example) {
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public boolean exists(String name) {
        return repository.existsByName(name);
    }

    @Override
    @Transactional
    public void delete(Integer integer) {
        repository.deleteById(integer);
    }

}

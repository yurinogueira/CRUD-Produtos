package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Client;
import org.springframework.data.domain.Example;

import java.util.List;

public interface ClientService {

    void save(Client client);

    Client get(Integer integer);

    List<Client> search(Example<Client> example);

    boolean exists(String name);

    void delete(Integer integer);
}

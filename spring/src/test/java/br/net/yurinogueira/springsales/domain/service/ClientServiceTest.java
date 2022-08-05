package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;


@SpringBootTest
class ClientServiceTest {

    @Autowired
    private ClientService testService;

    @Test
    void itShouldSave() {
        Client client = new Client();
        client.setName("Elaine");
        client.setDocument("17133662078");

        testService.save(client);

        Assertions.assertNotNull(client.getId());
    }

    @Test
    void getShouldReturnClient() {
        Client client = new Client();
        client.setName("Emily");
        client.setDocument("67812311031");

        testService.save(client);

        Client getClient = testService.get(client.getId());

        Assertions.assertEquals(client.getName(), getClient.getName());
    }

    @Test
    void getShouldReturnNull() {
        Client client = testService.get(10);

        Assertions.assertNull(client);
    }

    @Test
    void searchShouldReturnTwoClients() {
        Client firstClient = new Client();
        Client secondClient = new Client();
        firstClient.setName("Yuri Nogueira");
        firstClient.setDocument("56688306085");
        secondClient.setName("Tata Nogueira");
        secondClient.setDocument("23122102072");


        testService.save(firstClient);
        testService.save(secondClient);

        Client filter = new Client();
        filter.setName("Nogueira");
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Client> example = Example.of(filter, matcher);

        List<Client> clients = testService.search(example);

        Assertions.assertEquals(2, clients.size());
    }

    @Test
    void existsShouldReturnTrue() {
        Client client = new Client();
        client.setName("Sebastian Duarte");
        client.setDocument("68375975079");

        testService.save(client);

        boolean result = testService.exists(client.getName());

        Assertions.assertTrue(result);
    }

    @Test
    void shouldDeleteClientById() {
        Client client = new Client();
        client.setName("Pipoca");
        client.setDocument("91838567062");

        testService.save(client);
        testService.delete(client.getId());
        Client getClient = testService.get(client.getId());

        Assertions.assertNull(getClient);
    }

}
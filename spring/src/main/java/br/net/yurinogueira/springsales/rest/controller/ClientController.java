package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import br.net.yurinogueira.springsales.domain.service.ClientService;
import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/client/")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserServiceImpl userService;

    @GetMapping()
    public Client read(@AuthenticationPrincipal User userDetail) {
        String login = userDetail.getUsername();
        SystemUser user = userService.get(login);

        Client client =  user.getClient();
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return client;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal User userDetail, @RequestBody @Valid Client client) {
        String login = userDetail.getUsername();
        SystemUser user = userService.get(login);

        Client nativeClient =  user.getClient();
        if (nativeClient == null || client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        client.setId(nativeClient.getId());
        clientService.save(client);
    }

    @PostMapping("admin/")
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody @Valid Client client) {
        clientService.save(client);
        return client;
    }

    @DeleteMapping("admin/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Client client = clientService.get(id);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        clientService.delete(id);
    }

    @PutMapping("admin/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Client client) {
        Client nativeClient =  clientService.get(id);
        if (nativeClient == null || client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        client.setId(nativeClient.getId());
        clientService.save(client);
    }

    @GetMapping("admin/list/")
    public List<Client> list(Client filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Client> example = Example.of(filter, matcher);

        return clientService.search(example);
    }

}

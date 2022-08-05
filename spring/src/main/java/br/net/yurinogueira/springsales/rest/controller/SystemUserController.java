package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.Role;
import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import br.net.yurinogueira.springsales.domain.service.ClientService;
import br.net.yurinogueira.springsales.domain.service.RoleService;
import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;
import br.net.yurinogueira.springsales.rest.dto.CredentialsDTO;
import br.net.yurinogueira.springsales.rest.dto.SystemUserDTO;
import br.net.yurinogueira.springsales.rest.dto.TokenCheckDTO;
import br.net.yurinogueira.springsales.rest.dto.TokenDTO;
import br.net.yurinogueira.springsales.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class SystemUserController {

    private final UserServiceImpl userService;
    private final ClientService clientService;
    private final RoleService roleService;
    
    private final PasswordEncoder encoder;
    private final JWTService jwtService;

    @PostMapping("check/")
    @ResponseStatus(HttpStatus.OK)
    public TokenCheckDTO verify(@RequestBody @Valid TokenCheckDTO tokenCheckDTO) {
        String[] tokenSplited = tokenCheckDTO.getToken().split(" ");
        if (tokenSplited.length > 1 && jwtService.isValidToken(tokenSplited[1])) {
            return tokenCheckDTO;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token inválido");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenDTO create(@RequestBody @Valid SystemUserDTO user) {
        if (userService.existsByLogin(user.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já cadastrado");
        }

        Role role = roleService.get("USER");

        Client client = Client.builder()
                .name(user.getName())
                .document(user.getDocument())
                .build();
        clientService.save(client);

        String encryptedPassword = encoder.encode(user.getPassword());
        SystemUser systemUser = SystemUser.builder()
                .login(user.getLogin())
                .password(encryptedPassword)
                .client(client)
                .roles(List.of(role))
                .build();
        
        userService.save(systemUser);

        String token = jwtService.encodeToken(systemUser);

        return new TokenDTO(
            systemUser.getLogin(),
            token,
            systemUser.getRoles(),
            systemUser.getClient().getId()
        );
    }

    @PostMapping("auth/")
    public TokenDTO authenticate(@RequestBody CredentialsDTO credentials) {
        SystemUser user = SystemUser
                .builder()
                .login(credentials.getLogin())
                .password(credentials.getPassword())
                .build();

        userService.authenticate(user);
        user = userService.get(user.getLogin());

        String token = jwtService.encodeToken(user);

        return new TokenDTO(
            user.getLogin(),
            token,
            user.getRoles(),
            user.getClient().getId()
        );
    }

}

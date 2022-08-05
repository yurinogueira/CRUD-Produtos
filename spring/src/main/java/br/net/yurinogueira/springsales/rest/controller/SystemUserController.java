package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;
import br.net.yurinogueira.springsales.rest.dto.CredentialsDTO;
import br.net.yurinogueira.springsales.rest.dto.SystemUserDTO;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class SystemUserController {

    private final UserServiceImpl userService;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SystemUserDTO create(@RequestBody @Valid SystemUser user) {
        if (userService.existsByLogin(user.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já cadastrado");
        }

        String encryptedPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userService.save(user);

        return SystemUserDTO
                .builder()
                .id(user.getId())
                .login(user.getLogin())
                .roles(user.getRoles())
                .build();
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

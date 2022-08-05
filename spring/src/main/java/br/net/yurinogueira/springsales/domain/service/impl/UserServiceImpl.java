package br.net.yurinogueira.springsales.domain.service.impl;

import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import br.net.yurinogueira.springsales.domain.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final PasswordEncoder encoder;
    private final SystemUserRepository repository;

    @Transactional
    public SystemUser save(SystemUser user) {
        return repository.save(user);
    }

    @Transactional
    public SystemUser get(String login) {
        Optional<SystemUser> user = repository.findByLogin(login);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public void authenticate(SystemUser user) throws ResponseStatusException {
        UserDetails userDetails = loadUserByUsername(user.getLogin());
        boolean matches = encoder.matches(user.getPassword(), userDetails.getPassword());
        if (!matches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws ResponseStatusException {
        SystemUser user = repository
                .findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles())
                .disabled(false)
                .build();
    }

    public boolean existsByLogin(String login) {
        return repository.findByLogin(login).orElse(null) != null;
    }

}

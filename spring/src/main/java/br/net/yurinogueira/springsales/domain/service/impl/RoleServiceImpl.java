package br.net.yurinogueira.springsales.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import br.net.yurinogueira.springsales.domain.entity.Role;
import br.net.yurinogueira.springsales.domain.repository.RoleRepository;
import br.net.yurinogueira.springsales.domain.service.RoleService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository repository;

    @Override
    @Transactional
    public void save(Role role) {
        repository.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role get(String authority) {
        Optional<Role> role = repository.findByAuthority(authority);
        if (role.isEmpty()) {
            return null;
        }
        return role.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> search(Example<Role> example) {
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public boolean exists(String authority) {
        return repository.existsByAuthority(authority);
    }

    @Override
    @Transactional
    public void delete(Integer integer) {
        repository.deleteById(integer);
    }

}


package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Role;

import java.util.List;

import org.springframework.data.domain.Example;


public interface RoleService {
    
    void save(Role role);

    Role get(String authority);

    List<Role> search(Example<Role> example);

    boolean exists(String authority);

    void delete(Integer integer);

}

package br.net.yurinogueira.springsales.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.net.yurinogueira.springsales.domain.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
}

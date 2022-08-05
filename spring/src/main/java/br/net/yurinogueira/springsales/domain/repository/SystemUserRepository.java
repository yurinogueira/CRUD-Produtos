package br.net.yurinogueira.springsales.domain.repository;

import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {

    Optional<SystemUser> findByLogin(String login);

}

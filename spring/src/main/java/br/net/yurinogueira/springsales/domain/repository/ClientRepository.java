package br.net.yurinogueira.springsales.domain.repository;

import br.net.yurinogueira.springsales.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    boolean existsByName(String name);

}

package br.net.yurinogueira.springsales.domain.repository;

import br.net.yurinogueira.springsales.domain.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    boolean existsByDescription(String description);

}

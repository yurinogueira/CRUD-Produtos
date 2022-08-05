package br.net.yurinogueira.springsales.domain.repository;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}

package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Sale;
import org.springframework.data.domain.Example;

import java.util.List;

public interface SaleService {

    void save(Sale sale);

    Sale get(Integer integer);

    List<Sale> search(Example<Sale> example);

    boolean exists(String name);

    void delete(Integer integer);

}

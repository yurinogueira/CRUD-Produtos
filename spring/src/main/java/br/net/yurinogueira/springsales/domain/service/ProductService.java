package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Product;
import org.springframework.data.domain.Example;

import java.util.List;

public interface ProductService {

    void save(Product product);

    Product get(Integer integer);

    List<Product> search(Example<Product> example);

    boolean exists(String name);

    void delete(Integer integer);

}

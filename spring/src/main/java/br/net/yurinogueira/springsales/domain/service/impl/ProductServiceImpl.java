package br.net.yurinogueira.springsales.domain.service.impl;

import br.net.yurinogueira.springsales.domain.entity.Product;
import br.net.yurinogueira.springsales.domain.repository.ProductRepository;
import br.net.yurinogueira.springsales.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    @Transactional
    public void save(Product product) {
        repository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product get(Integer integer) {
        Optional<Product> product = repository.findById(integer);
        if (product.isEmpty()) {
            return null;
        }
        return product.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> search(Example<Product> example) {
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public boolean exists(String name) {
        return repository.existsByName(name);
    }

    @Override
    @Transactional
    public void delete(Integer integer) {
        repository.deleteById(integer);
    }

}

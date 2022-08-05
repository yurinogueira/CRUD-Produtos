package br.net.yurinogueira.springsales.domain.service.impl;

import br.net.yurinogueira.springsales.domain.entity.Sale;
import br.net.yurinogueira.springsales.domain.repository.SaleRepository;
import br.net.yurinogueira.springsales.domain.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;

    @Override
    @Transactional
    public void save(Sale sale) {
        repository.save(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public Sale get(Integer integer) {
        Optional<Sale> sale = repository.findById(integer);
        if (sale.isEmpty()) {
            return null;
        }
        return sale.get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> search(Example<Sale> example) {
        return repository.findAll(example);
    }

    @Override
    @Transactional
    public boolean exists(String description) {
        return repository.existsByDescription(description);
    }

    @Override
    @Transactional
    public void delete(Integer integer) {
        repository.deleteById(integer);
    }

}

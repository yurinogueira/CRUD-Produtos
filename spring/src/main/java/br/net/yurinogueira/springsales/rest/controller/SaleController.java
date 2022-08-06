package br.net.yurinogueira.springsales.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.net.yurinogueira.springsales.domain.entity.Sale;
import br.net.yurinogueira.springsales.domain.service.SaleService;
import br.net.yurinogueira.springsales.rest.dto.SaleInfoDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sale/")
@RequiredArgsConstructor
public class SaleController {
    
    private final SaleService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleInfoDTO create(@RequestBody @Valid Sale sale) {
        service.save(sale);
        return getSaleInfoDTO(sale);
    }

    @GetMapping("{id}/")
    public SaleInfoDTO read(@PathVariable Integer id) {
        Sale sale = service.get(id);
        if (sale == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return getSaleInfoDTO(sale);
    }

    @PutMapping("{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Sale sale) {
        Sale nativeSale = service.get(id);
        if (nativeSale == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        sale.setId(nativeSale.getId());
        service.save(sale);
    }

    @DeleteMapping("{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Sale sale = service.get(id);
        if (sale == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        service.delete(id);
    }

    @GetMapping
    public List<SaleInfoDTO> list(Sale filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Sale> example = Example.of(filter, matcher);

        List<Sale> sales = service.search(example);
        return sales.stream().map(sale -> getSaleInfoDTO(sale)).collect(Collectors.toList());
    }

    private SaleInfoDTO getSaleInfoDTO(Sale sale) {
        return SaleInfoDTO.builder()
                .id(sale.getId())
                .description(sale.getDescription())
                .type(sale.getType())
                .saleCheckAmount(sale.getSaleCheckAmount())
                .saleAmount(sale.getSaleAmount())
                .salePrice(sale.getSalePrice())
                .build();
    }
}

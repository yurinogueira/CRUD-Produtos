package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.domain.entity.Product;
import br.net.yurinogueira.springsales.domain.entity.Sale;
import br.net.yurinogueira.springsales.domain.service.ProductService;
import br.net.yurinogueira.springsales.domain.service.SaleService;
import br.net.yurinogueira.springsales.rest.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product/")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SaleService saleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody @Valid ProductDTO productBase) {
        Sale sale = null;
        if (productBase.getSale() != null) {
            saleService.get(productBase.getSale());
            if (sale == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        Product product = Product.builder()
            .name(productBase.getName())
            .description(productBase.getDescription())
            .basePrice(productBase.getPrice())
            .sale(sale)
            .build();

        productService.save(product);
        return product;
    }

    @GetMapping("{id}/")
    public Product read(@PathVariable Integer id) {
        Product product = productService.get(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return product;
    }

    @PutMapping("{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid ProductDTO productBase) {
        Product nativeProduct = productService.get(id);
        if (nativeProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Sale sale = null;
        if (productBase.getSale() != null) {
            saleService.get(productBase.getSale());
            if (sale == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        Product updatedProduct = Product.builder()
            .id(nativeProduct.getId())
            .name(productBase.getName())
            .description(productBase.getDescription())
            .basePrice(productBase.getPrice())
            .sale(sale)
            .build();
        productService.save(updatedProduct);
    }

    @DeleteMapping("{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        Product product = productService.get(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        productService.delete(id);
    }

    @GetMapping
    public List<Product> list(Product filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Product> example = Example.of(filter, matcher);

        return productService.search(example);
    }

}

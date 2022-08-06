package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;


@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService testService;

    @Test
    void itShouldSave() {
        Product product = new Product();
        product.setName("Coca-Nola");
        product.setDescription("Refrigerante sem Gás");
        product.setBasePrice(4.5);

        testService.save(product);

        Assertions.assertNotNull(product.getId());
    }

    @Test
    void getShouldReturnProduct() {
        Product product = new Product();
        product.setName("Pepde");
        product.setDescription("Suco com Gás");
        product.setBasePrice(4.0);

        testService.save(product);

        Product getProduct = testService.get(product.getId());

        Assertions.assertEquals(product.getName(), getProduct.getName());
    }

    @Test
    void getShouldReturnNull() {
        Product product = testService.get(35);

        Assertions.assertNull(product);
    }

    @Test
    void searchShouldReturnTwoClients() {
        Product firstProduct = new Product();
        Product secondProduct = new Product();
        firstProduct.setName("Feijoada sabor Batata");
        firstProduct.setDescription("Realmente possui sabor de batata");
        firstProduct.setBasePrice(10.5);
        secondProduct.setName("Feijoada sabor Churrasco");
        firstProduct.setDescription("Não possui nenhum sabor de churrasco");
        firstProduct.setBasePrice(6.5);

        testService.save(firstProduct);
        testService.save(secondProduct);

        Product filter = new Product();
        filter.setName("Feijoada");
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Product> example = Example.of(filter, matcher);

        List<Product> products = testService.search(example);

        Assertions.assertEquals(2, products.size());
    }

    @Test
    void existsShouldReturnTrue() {
        Product product = new Product();
        product.setName("Batatinhas Doces");
        product.setDescription("Batatinhas feitas com Açúcar");
        product.setBasePrice(3.0);

        testService.save(product);

        boolean result = testService.exists(product.getName());

        Assertions.assertTrue(result);
    }

    @Test
    void shouldDeleteProductById() {
        Product product = new Product();
        product.setName("Pipoca de Arroz");
        product.setDescription("Arroz em formato de pipoca");
        product.setBasePrice(3.5);

        testService.save(product);
        testService.delete(product.getId());
        Product getProduct = testService.get(product.getId());

        Assertions.assertNull(getProduct);
    }

}
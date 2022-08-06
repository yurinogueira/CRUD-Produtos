package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.Product;
import br.net.yurinogueira.springsales.rest.dto.CartDTO;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;
import br.net.yurinogueira.springsales.rest.dto.ItemDTO;
import br.net.yurinogueira.springsales.rest.dto.ItemInfoDTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Test
    void itShouldSave() {
        Product product = new Product();
        product.setName("Cona-Cola");
        product.setDescription("Cola de Cona");
        product.setBasePrice(new BigDecimal("20.4"));
        productService.save(product);

        Client client = new Client();
        client.setName("Silvio Cariocas");
        client.setDocument("70719877008");
        clientService.save(client);

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setAmount(2);
        itemDTO.setProduct(product.getId());

        CartDTO cartDTO = new CartDTO();
        cartDTO.setClient(client.getId());
        cartDTO.setItems(List.of(itemDTO));

        CartInfoDTO cart = cartService.save(cartDTO);
        List<ItemInfoDTO> items = cart.getItems();
        ItemInfoDTO item = items.get(0);

        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(product.getName(), item.getName());
        Assertions.assertEquals(client.getName(), cart.getClientName());
    }

    @Test
    void itShouldReturn() {
        Product product = new Product();
        product.setName("Ruimbrill");
        product.setDescription("Limpador de parabrisa de carro");
        product.setBasePrice(new BigDecimal("1.5"));
        productService.save(product);

        Client client = new Client();
        client.setName("Alexandro Didi");
        client.setDocument("81500868035");
        clientService.save(client);

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setAmount(4);
        itemDTO.setProduct(product.getId());

        CartDTO cartDTO = new CartDTO();
        cartDTO.setClient(client.getId());
        cartDTO.setItems(List.of(itemDTO));

        CartInfoDTO cartInfoDTO = cartService.save(cartDTO);

        Assertions.assertEquals("PENDING", cartInfoDTO.getStatus());
        Assertions.assertEquals(client.getName(), cartInfoDTO.getClientName());
    }

}
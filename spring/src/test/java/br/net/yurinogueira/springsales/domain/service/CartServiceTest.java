package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.Item;
import br.net.yurinogueira.springsales.domain.entity.Product;
import br.net.yurinogueira.springsales.rest.dto.CartDTO;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;
import br.net.yurinogueira.springsales.rest.dto.ItemDTO;
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
        product.setBasePrice(BigDecimal.valueOf(20.4));
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

        Cart cart = cartService.save(cartDTO);
        List<Item> items = cart.getItems();
        Item item = items.get(0);

        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(product.getName(), item.getProduct().getName());
        Assertions.assertEquals(client.getName(), cart.getClient().getName());
    }

    @Test
    void itShouldReturn() {
        Product product = new Product();
        product.setName("Ruimbrill");
        product.setDescription("Limpador de parabrisa de carro");
        product.setBasePrice(BigDecimal.valueOf(1.5));
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

        Cart cart = cartService.save(cartDTO);
        CartInfoDTO cartInfoDTO = cartService.get(cart.getId());

        Assertions.assertEquals("PENDING", cartInfoDTO.getStatus());
        Assertions.assertEquals(client.getName(), cartInfoDTO.getClientName());
    }

}
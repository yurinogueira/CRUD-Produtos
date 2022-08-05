package br.net.yurinogueira.springsales.domain.service.impl;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.Item;
import br.net.yurinogueira.springsales.domain.entity.Product;
import br.net.yurinogueira.springsales.domain.enums.CartStatus;
import br.net.yurinogueira.springsales.domain.repository.CartRepository;
import br.net.yurinogueira.springsales.domain.repository.ClientRepository;
import br.net.yurinogueira.springsales.domain.repository.ItemRepository;
import br.net.yurinogueira.springsales.domain.repository.ProductRepository;
import br.net.yurinogueira.springsales.domain.service.CartService;
import br.net.yurinogueira.springsales.exception.RulesException;
import br.net.yurinogueira.springsales.rest.dto.CartDTO;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;
import br.net.yurinogueira.springsales.rest.dto.ItemDTO;
import br.net.yurinogueira.springsales.rest.dto.ItemInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Cart save(CartDTO dto) {
        Integer clientId = dto.getClient();
        Client client = clientRepository
                .findById(clientId)
                .orElseThrow(() -> {
                    String errorMessage = "O cliente de código %d não foi encontrado";
                    return new RulesException(String.format(errorMessage, clientId));
                });

        Cart cart = new Cart();
        cart.setDate(LocalDate.now());
        cart.setClient(client);
        cart.setStatus(CartStatus.PENDING);

        List<Item> items = getItems(cart, dto.getItems());

        cartRepository.save(cart);
        itemRepository.saveAll(items);

        cart.setItems(items);

        return cart;
    }

    @Override
    @Transactional
    public CartInfoDTO get(Integer id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isEmpty()) {
            String errorMessage = "O pedido de código %d não foi encontrado";
            throw new RulesException(String.format(errorMessage, id));
        }

        return getCartInfoDTO(cart.get());
    }

    @Override
    @Transactional
    public void patch(Integer id, CartStatus status) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isEmpty()) {
            String errorMessage = "O pedido de código %d não foi encontrado";
            throw new RulesException(String.format(errorMessage, id));
        }

        Cart actualCart = cart.get();
        actualCart.setStatus(status);
        cartRepository.save(actualCart);
    }

    private List<Item> getItems(Cart cart, List<ItemDTO> itemDTOs) {
        if (itemDTOs.isEmpty()) {
            throw new RulesException("A lista de items não pode ser vazia");
        }

        return itemDTOs.stream().map(dto -> {
            Integer productId = dto.getProduct();
            Product product = productRepository
                    .findById(productId)
                    .orElseThrow(() -> {
                        String errorMessage = "O produto de código %d não foi encontrado";
                        return new RulesException(String.format(errorMessage, productId));
                    });

            final Item item = new Item();
            item.setAmount(dto.getAmount());
            item.setCart(cart);
            item.setProduct(product);
            item.setPrice(product.getBasePrice());

            return item;
        }).collect(Collectors.toList());
    }

    private CartInfoDTO getCartInfoDTO(Cart cart) {
        List<ItemInfoDTO> items = getItemsInfoDTO(cart.getItems());

        return CartInfoDTO
                .builder()
                .id(cart.getId())
                .clientName(cart.getClient().getName())
                .clientDocument(cart.getClient().getDocument())
                .cartDate(cart.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(cart.getStatus().name())
                .items(items)
                .build();
    }

    private List<ItemInfoDTO> getItemsInfoDTO(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }

        return items.stream().map(item ->
                ItemInfoDTO
                        .builder()
                        .amount(item.getAmount())
                        .name(item.getProduct().getName())
                        .description(item.getProduct().getDescription())
                        .unitPrice(item.getProduct().getBasePrice())
                        .unitPaidPrice(item.getPrice())
                        .build()
        ).collect(Collectors.toList());
    }

}

package br.net.yurinogueira.springsales.domain.service.impl;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import br.net.yurinogueira.springsales.domain.entity.Client;
import br.net.yurinogueira.springsales.domain.entity.Item;
import br.net.yurinogueira.springsales.domain.entity.Product;
import br.net.yurinogueira.springsales.domain.entity.Sale;
import br.net.yurinogueira.springsales.domain.enums.CartStatus;
import br.net.yurinogueira.springsales.domain.enums.SaleType;
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

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
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
    public CartInfoDTO save(CartDTO dto) {
        Integer clientId = dto.getClient();
        Client client = clientRepository
                .findById(clientId)
                .orElseThrow(() -> {
                    String errorMessage = "O cliente de código %d não foi encontrado";
                    return new RulesException(String.format(errorMessage, clientId));
                });

        Cart cart = new Cart();
        cart.setDate(LocalDateTime.now());
        cart.setClient(client);
        cart.setStatus(CartStatus.PENDING);

        List<Item> items = getItems(cart, dto.getItems());

        cartRepository.save(cart);
        itemRepository.saveAll(items);

        cart.setItems(items);

        return getCartInfoDTO(cart);
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
    @Transactional(readOnly = true)
    public List<CartInfoDTO> search(Integer id) {
        Client client = clientRepository
            .findById(id)
            .orElseThrow(() -> {
                String errorMessage = "O cliente de código %d não foi encontrado";
                return new RulesException(String.format(errorMessage, id));
            });
        Cart exampleCart = new Cart();

        exampleCart.setClient(client);

        List<Cart> carts = cartRepository.findAll(Example.of(exampleCart));

        return carts.stream().map(
            (cart) -> getCartInfoDTO(cart)
        ).collect(Collectors.toList());
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
            item.setPrice(product.getBasePrice());
            item.setAmount(dto.getAmount());
            item.setCart(cart);
            item.setProduct(product);

            return item;
        }).collect(Collectors.toList());
    }

    private CartInfoDTO getCartInfoDTO(Cart cart) {
        List<ItemInfoDTO> items = getItemsInfoDTO(cart.getItems());

        Double totalPrice = 0.0;
        for (ItemInfoDTO item : items) {
            totalPrice += item.getTotalPrice();
        }

        return CartInfoDTO
                .builder()
                .id(cart.getId())
                .clientName(cart.getClient().getName())
                .clientDocument(cart.getClient().getDocument())
                .cartDate(cart.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .status(cart.getStatus().name())
                .items(items)
                .totalPrice(totalPrice)
                .build();
    }

    private List<ItemInfoDTO> getItemsInfoDTO(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }

        return items.stream().map(item -> {
            Sale sale = item.getProduct().getSale();
            Integer amount = item.getAmount();
            Double price = item.getPrice();
            Double totalPrice = 0.0;
            String saleDescription;

            if (sale != null) {
                int residue = amount % sale.getSaleCheckAmount();
                int amountOfSale = amount / sale.getSaleCheckAmount();
                saleDescription = sale.getDescription();
                if (sale.getType() == SaleType.AMOUNT_PER_AMOUNT) {
                    int total = residue + (amountOfSale * sale.getSaleAmount());
                    totalPrice += (price * total);
                }
                else {
                    Double totalBaseCost = price * residue;
                    Double totalSaleCost = sale.getSalePrice() * amountOfSale;
                    totalPrice += (totalBaseCost + totalSaleCost);
                }
            }
            else {
                totalPrice += (price * amount);
                saleDescription = "";
            }

            ItemInfoDTO itemInfoDTO =  ItemInfoDTO
                    .builder()
                    .amount(amount)
                    .name(item.getProduct().getName())
                    .description(item.getProduct().getDescription())
                    .unitPrice(price)
                    .totalPrice(totalPrice)
                    .promotionDescription(saleDescription)
                    .build();
            return itemInfoDTO;
        }
               
        ).collect(Collectors.toList());
    }

}

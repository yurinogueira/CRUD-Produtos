package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import br.net.yurinogueira.springsales.domain.enums.CartStatus;
import br.net.yurinogueira.springsales.domain.service.CartService;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;
import br.net.yurinogueira.springsales.rest.dto.CartDTO;
import br.net.yurinogueira.springsales.rest.dto.CartStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart/")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer create(@RequestBody @Valid CartDTO dto) {
        Cart cart = service.save(dto);
        return cart.getId();
    }

    @GetMapping("{id}/")
    public CartInfoDTO read(@PathVariable Integer id) {
        return service.get(id);
    }

    @PatchMapping("{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody CartStatusDTO cartStatusDTO) {
        String status = cartStatusDTO.getStatus();
        service.patch(id, CartStatus.valueOf(status));
    }

}

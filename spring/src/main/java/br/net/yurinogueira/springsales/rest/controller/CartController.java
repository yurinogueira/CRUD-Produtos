package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.domain.entity.SystemUser;
import br.net.yurinogueira.springsales.domain.enums.CartStatus;
import br.net.yurinogueira.springsales.domain.service.CartService;
import br.net.yurinogueira.springsales.domain.service.impl.UserServiceImpl;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;
import br.net.yurinogueira.springsales.rest.dto.CartRequestDTO;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart/")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartInfoDTO create(@AuthenticationPrincipal User userDetail, @RequestBody @Valid CartRequestDTO dto) {
        String login = userDetail.getUsername();
        SystemUser user = userService.get(login);

        CartDTO cartDTO = CartDTO.builder()
                .client(user.getClient().getId())
                .items(dto.getItems())
                .build();
        CartInfoDTO cart = cartService.save(cartDTO);
        return cart;
    }

    @GetMapping("list/")
    public List<CartInfoDTO> list(@AuthenticationPrincipal User userDetail) {
        String login = userDetail.getUsername();
        SystemUser user = userService.get(login);
        return cartService.search(user.getClient().getId());
    }

    @GetMapping("{id}/")
    public CartInfoDTO read(@PathVariable Integer id) {
        CartInfoDTO cart = cartService.get(id);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return cartService.get(id);
    }

    @PatchMapping("{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody CartStatusDTO cartStatusDTO) {
        String status = cartStatusDTO.getStatus();
        cartService.patch(id, CartStatus.valueOf(status));
    }

}

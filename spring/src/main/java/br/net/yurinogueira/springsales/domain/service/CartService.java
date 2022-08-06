package br.net.yurinogueira.springsales.domain.service;

import java.util.List;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import br.net.yurinogueira.springsales.domain.enums.CartStatus;
import br.net.yurinogueira.springsales.rest.dto.CartDTO;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;

public interface CartService {

    Cart save(CartDTO dto);

    CartInfoDTO get(Integer id);

    List<CartInfoDTO> search(Integer id);

    void patch(Integer id, CartStatus status);

}

package br.net.yurinogueira.springsales.domain.service;

import br.net.yurinogueira.springsales.domain.entity.Cart;
import br.net.yurinogueira.springsales.domain.enums.CartStatus;
import br.net.yurinogueira.springsales.rest.dto.CartDTO;
import br.net.yurinogueira.springsales.rest.dto.CartInfoDTO;

public interface CartService {

    Cart save(CartDTO dto);

    CartInfoDTO get(Integer id);

    void patch(Integer id, CartStatus status);

}

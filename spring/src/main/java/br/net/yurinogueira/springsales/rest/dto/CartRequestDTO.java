package br.net.yurinogueira.springsales.rest.dto;

import br.net.yurinogueira.springsales.annotations.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequestDTO {

    @NotEmptyList(message = "{error.cart.items-needed}")
    private List<ItemDTO> items;

}

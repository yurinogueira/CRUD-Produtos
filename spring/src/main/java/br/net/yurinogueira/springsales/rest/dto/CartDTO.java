package br.net.yurinogueira.springsales.rest.dto;

import br.net.yurinogueira.springsales.annotations.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    @NotNull(message = "{error.client.code-needed}")
    private Integer client;

    @NotEmptyList(message = "{error.cart.items-needed}")
    private List<ItemDTO> items;

}

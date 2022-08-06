package br.net.yurinogueira.springsales.rest.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @NotNull(message = "{error.product.name-needed}")
    private String name;

    @NotNull(message = "{error.product.description-needed}")
    private String description;

    private Integer sale;

    @NotNull(message = "{error.product.price-needed}")
    private BigDecimal price;

}

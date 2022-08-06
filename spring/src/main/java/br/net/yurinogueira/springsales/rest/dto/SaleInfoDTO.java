package br.net.yurinogueira.springsales.rest.dto;

import br.net.yurinogueira.springsales.domain.enums.SaleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleInfoDTO {

    private Integer id;

    private String description;

    private SaleType type;

    private Integer saleCheckAmount;

    private Integer saleAmount;

    private Double salePrice;

}

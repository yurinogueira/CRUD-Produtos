package br.net.yurinogueira.springsales.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfoDTO {

    private String name;
    private String description;
    private BigDecimal unitPaidPrice;
    private BigDecimal unitPrice;
    private Integer amount;

}

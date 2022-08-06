package br.net.yurinogueira.springsales.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfoDTO {

    private String name;
    private String description;
    private Double totalPrice;
    private Double unitPrice;
    private String promotionDescription;
    private Integer amount;

}

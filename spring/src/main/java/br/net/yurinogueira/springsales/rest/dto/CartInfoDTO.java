package br.net.yurinogueira.springsales.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartInfoDTO {

    private Integer id;
    private String clientName;
    private String clientDocument;
    private String cartDate;
    private String status;
    private List<ItemInfoDTO> items;

}

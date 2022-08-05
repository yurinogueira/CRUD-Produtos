package br.net.yurinogueira.springsales.rest.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemUserDTO {

    private Integer id;
    private String login;
    private String[] roles;

}

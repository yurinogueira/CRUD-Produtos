package br.net.yurinogueira.springsales.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String login;
    private String token;
    private String[] roles;
    private Integer clientId;

}

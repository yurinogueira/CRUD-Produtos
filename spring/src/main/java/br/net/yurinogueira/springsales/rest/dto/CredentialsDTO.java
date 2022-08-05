package br.net.yurinogueira.springsales.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialsDTO {

    private String login;
    private String password;

}

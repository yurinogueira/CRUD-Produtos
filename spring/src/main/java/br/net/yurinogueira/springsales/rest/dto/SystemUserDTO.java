package br.net.yurinogueira.springsales.rest.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserDTO {

    @NotNull(message = "{error.user.login-needed}")
    private String login;

    @NotNull(message = "{error.user.password-needed}")
    private String password;

    @NotNull(message = "{error.client.name-needed}")
    private String name;

    @NotNull(message = "{error.client.cpf-needed}")
    @CPF(message = "{error.client.cpf-invalid}")
    private String document;

}

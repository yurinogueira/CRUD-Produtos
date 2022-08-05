package br.net.yurinogueira.springsales.rest.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenCheckDTO {

    @NotNull(message = "{error.auth.token-needed}")
    private String token;

}

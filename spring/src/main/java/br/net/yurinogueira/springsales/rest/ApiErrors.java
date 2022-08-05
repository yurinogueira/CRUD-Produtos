package br.net.yurinogueira.springsales.rest;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private final List<String> errors;

    public ApiErrors(String... errorMessages) {
        this.errors = Arrays.asList(errorMessages);
    }

}

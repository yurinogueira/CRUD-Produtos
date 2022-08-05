package br.net.yurinogueira.springsales.rest.controller;

import br.net.yurinogueira.springsales.exception.RulesException;
import br.net.yurinogueira.springsales.rest.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RulesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRulesException(RulesException exception) {
        return new ApiErrors(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException exception) {
        return new ApiErrors(exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String errorMessage = error.getDefaultMessage();
                    String defaultMessage = "Um erro ocorreu";
                    return errorMessage != null ? errorMessage : defaultMessage;
                }).toArray(String[]::new)
        );
    }


}

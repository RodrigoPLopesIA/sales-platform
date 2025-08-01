package br.com.rodrigolopesdev.product_api.dtos;

import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.Map;

public record ErrorMessageDTO(HttpStatus status, String message, Map<String, String> errors) {

    public ErrorMessageDTO(HttpStatus status, String message) {
        this(status, message, null);
    }

}

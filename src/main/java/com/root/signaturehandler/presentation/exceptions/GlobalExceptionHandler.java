package com.root.signaturehandler.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> exceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, Object> responseMap = new LinkedHashMap<>();

        List<String> listErrors =
                exception.getFieldErrors().stream()
                        .map(error -> error.getDefaultMessage()).collect(Collectors.toList());

        responseMap.put("statusCode", 422);
        responseMap.put("errors", listErrors);

        return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

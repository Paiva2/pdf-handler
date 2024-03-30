package com.root.signaturehandler.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> dtoExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, Object> responseMap = new LinkedHashMap<>();

        List<String> listErrors =
                exception.getFieldErrors().stream()
                        .map(error -> error.getDefaultMessage()).collect(Collectors.toList());

        responseMap.put("statusCode", 422);
        responseMap.put("errors", listErrors);

        return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        HashMap<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("timeStamp", LocalDateTime.now());
        errorMap.put("status", 403);
        errorMap.put("error", "Forbidden");
        errorMap.put("message", ex.getMessage());

        return new ResponseEntity<>(errorMap, HttpStatus.FORBIDDEN);
    }
}

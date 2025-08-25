package com.fastcampus.crash.exception;

import com.fastcampus.crash.model.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(ClientErrorException e) {

       return new ResponseEntity<>(new ErrorResponse(e.getStatus(),e.getMessage()), e.getStatus());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException e) {

        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        return ResponseEntity.internalServerError().build();
    }
}

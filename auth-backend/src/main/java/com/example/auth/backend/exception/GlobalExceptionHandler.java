package com.example.auth.backend.exception;

import com.example.auth.backend.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> badRequest(IllegalArgumentException ex,HttpServletRequest req){
        return build(400,"Bad Request",ex.getMessage(),req);
    }
    @ExceptionHandler({BadCredentialsException.class})
    ResponseEntity<ErrorResponse> unauthorized(Exception ex,HttpServletRequest req){
        return build(401,"Unauthorized","Invalid username or password",req);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex,HttpServletRequest req){
        String message=ex.getBindingResult().getFieldErrors().stream()
            .findFirst().map(e->e.getField()+": "+e.getDefaultMessage()).orElse("Validation failed");
        return build(400,"Bad Request",message,req);
    }
    private ResponseEntity<ErrorResponse> build(int status,String error,String message,HttpServletRequest req){
        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(),status,error,message,req.getRequestURI()));
    }
}

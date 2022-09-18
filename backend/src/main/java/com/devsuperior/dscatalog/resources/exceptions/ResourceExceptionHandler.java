package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(ResourceNotFoundException entityNotFoundException, HttpServletRequest request) {
        StandardError standardError = new StandardError();
        standardError.setError("resource_not_found");
        standardError.setMessage(entityNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException databaseException, HttpServletRequest request) {
        StandardError standardError = new StandardError();
        standardError.setError("database_exception");
        standardError.setMessage(databaseException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }
}

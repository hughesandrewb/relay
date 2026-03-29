package com.andhug.relay.shared.infrastructure.web;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.exception.UnauthorizedException;
import com.andhug.relay.shared.infrastructure.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    log.warn("NotFoundException occurred: {}", ex.getMessage());
    return ResponseEntity.status(404).body(errorResponse);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    log.warn("UnauthorizedException occurred: {}", ex.getMessage());
    return ResponseEntity.status(401).body(errorResponse);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    log.error("DomainException occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(400).body(errorResponse);
  }
}

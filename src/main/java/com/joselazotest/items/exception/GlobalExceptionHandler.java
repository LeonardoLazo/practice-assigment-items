package com.joselazotest.items.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controller.
 * Intercepts application exceptions and maps them to standardized HTTP error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String STR_ERROR = "error";

    /**
     * Handles payload validation failures triggered by {@code @Valid} annotations.
     *
     * @param exception the exception containing the binding results and field errors
     * @return HTTP 400 (Bad Request) with a map of field names to validation messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles malformed JSON payloads and unreadable HTTP requests.
     *
     * @param exception the exception triggered by unparseable request data
     * @return an HTTP 400 (Bad Request) response containing a standardized error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMalformedJsonException(HttpMessageNotReadableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(STR_ERROR, "Malformed JSON request or invalid data structure."));
    }

    /**
     * Maps a {@link ResourceAlreadyExistsException} to an HTTP 409 (Conflict) response.
     *
     * @param exception the thrown exception detailing the resource conflict
     * @return a 409 status response containing the error message payload
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleConflict(ResourceAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(STR_ERROR, exception.getMessage()));
    }

    /**
     * Handles unsupported HTTP method requests (HTTP 405).
     *
     * @param exception the exception triggered by the invalid HTTP method
     * @return a 405 Method Not Allowed response containing a structured error message
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of(STR_ERROR, "HTTP method not supported for this endpoint."));
    }

    /**
     * Handles unsupported media type errors (HTTP 415).
     *
     * @param exception the caught media type exception
     * @return a 415 status response prompting the use of application/json
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Map.of(STR_ERROR, "Unsupported Media Type. Please use application/json."));
    }
}

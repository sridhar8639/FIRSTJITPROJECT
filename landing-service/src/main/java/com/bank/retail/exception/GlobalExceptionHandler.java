package com.bank.retail.exception;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
 
import com.bank.retail.api.dto.GenericResponse;
import com.bank.retail.api.dto.ResultUtilVO;
import com.bank.retail.constants.AppConstant;
 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path.Node;
import lombok.extern.slf4j.Slf4j;
 
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
   
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(
	        MethodArgumentNotValidException ex, WebRequest request) {
	    log.error("Validation error occurred", ex);
	    Map<String, String> fieldErrors = new LinkedHashMap<>();
	    ex.getBindingResult().getAllErrors().forEach(error -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        fieldErrors.put(fieldName, errorMessage);
	    });
	    Map<String, Object> responseBody = new LinkedHashMap<>();
	    responseBody.put("timestamp", LocalDateTime.now().toString());
	    responseBody.put("status", HttpStatus.BAD_REQUEST.value());
	    responseBody.put("error", "Validation Failed");
	    responseBody.put("path", request.getDescription(false).replace("uri=", ""));
	    responseBody.put("details", fieldErrors);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}
 
   
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {
 
        log.error("Constraint violation error occurred", ex);
 
        Map<String, String> fieldErrors = new LinkedHashMap<>();
 
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = "";
            for (Node  node : violation.getPropertyPath()) {
                fieldName = node.getName();
            }
            fieldErrors.put(fieldName, violation.getMessage());
        }
 
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Validation failed");
        responseBody.put("path", request.getRequestURI());
        responseBody.put("details", fieldErrors);
        return ResponseEntity.badRequest().body(responseBody);
    }
 
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        log.error("Illegal argument error occurred", ex);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Illegal Argument");
        responseBody.put("message", "The request contains an invalid argument. Please contact support with the timestamp above.");
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
 
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(MissingRequestHeaderException ex) {
        log.warn("Missing required header: {}", ex.getHeaderName());
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Missing Request Header");
        error.put("message", "A required request header is missing.");
        error.put("missingHeader", ex.getHeaderName());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        log.error("Illegal state error occurred", ex);
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Illegal State");
        error.put("message", "The request could not be completed due to the current state of the resource.");
        error.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
 
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(
            NullPointerException ex, WebRequest request) {
        log.error("Null pointer error occurred", ex);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put("error", "Null Pointer Exception");
        responseBody.put("message", "Internal server error: Null reference encountered");
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
 
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, Object>> handleNumberFormatException(
            NumberFormatException ex, WebRequest request) {
        log.error("Number format error occurred", ex);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", "Number Format Error");
        responseBody.put("message", "The request contains an invalid numeric value. Please contact support with the timestamp above.");
        responseBody.put("details", "Check numeric fields like amount or ID values");
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
 
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request) {
        log.error("No resource found for URL: {}", request.getRequestURI(), ex);
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Resource Not Found");
        errorResponse.put("message", "The requested URL does not exist or is invalid.");
        errorResponse.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
 
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Invalid or missing JSON request body: {}", ex.getMessage());
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Invalid or Missing JSON Body");
        error.put("message", "The request body is missing, empty, or contains invalid JSON syntax.");
        error.put("details", "Verify the request body matches the expected format.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        log.error("Runtime error occurred: {}", ex.getMessage(), ex);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put("error", "Runtime Exception");
        responseBody.put("message", "An internal error occurred. Please contact support with the timestamp above.");
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now().toString());
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put("error", "Internal Server Error");
        responseBody.put("message", "An internal error occurred. Please contact support with the timestamp above.");
        responseBody.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
 
 
    
}
 
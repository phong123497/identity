package com.devteria.identityservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devteria.identityservice.dto.request.ApiResponse;



@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Object>> handleException (Exception exception) {
        
     
        ApiResponse<Object>  apiResponse  = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMeassge());

        return ResponseEntity.badRequest().body(apiResponse);
        
    }


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Object>> handleAppException (AppException exception) {
        ErrorCode  errorCode  =  exception.getErrorCode();
        ApiResponse<Object>  apiResponse  = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMeassge());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<Object>> handleAccessDeniedException (AccessDeniedException exception) {
        ErrorCode  errorCode  =  ErrorCode.UNAUTHORIZED;
        ApiResponse<Object>  apiResponse  = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMeassge());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
                            
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException (MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode  errorCode = ErrorCode.valueOf(enumKey);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMeassge());
        
        return  ResponseEntity.badRequest().body(apiResponse);
    }

}

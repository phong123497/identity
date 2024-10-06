


package com.dev.identityservice.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public  class AppException extends RuntimeException {

    
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode){
        super();
        this.errorCode = errorCode ;
    }

}
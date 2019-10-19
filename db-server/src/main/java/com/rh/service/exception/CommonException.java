package com.rh.service.exception;

public class CommonException extends Exception{
    String message;

    public CommonException(int exceptionCode, String exceptionMessage) {
        super(exceptionMessage);
        this.message = exceptionMessage;
    }
}

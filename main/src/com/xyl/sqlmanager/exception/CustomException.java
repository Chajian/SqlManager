package com.xyl.sqlmanager.exception;

public class CustomException extends RuntimeException {
    private String code;

    public CustomException(String message) {
        super(message);
    }
    public CustomException(String message,String code) {
        super(message);
        this.code = code;
        ExceptionHandlingService.getExceptionHandlingService().handleException(this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
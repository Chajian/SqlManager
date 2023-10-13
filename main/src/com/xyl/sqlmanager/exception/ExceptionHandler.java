package com.xyl.sqlmanager.exception;

@FunctionalInterface
public interface ExceptionHandler {
    void handleException(CustomException exception);
}

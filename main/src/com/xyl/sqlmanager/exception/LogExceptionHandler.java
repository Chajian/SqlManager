package com.xyl.sqlmanager.exception;

import java.util.ArrayList;
import java.util.List;

public class LogExceptionHandler implements ExceptionHandler{

    List<String> logInfo = new ArrayList<>();
    @Override
    public void handleException(CustomException exception) {
        System.out.println(String.valueOf(System.currentTimeMillis())+exception.getCode()+":"+exception.getMessage());
        logInfo.add(String.valueOf(System.currentTimeMillis())+exception.getCode()+":"+exception.getMessage());
    }
}

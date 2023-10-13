package com.xyl.sqlmanager.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式
 */
public class ExceptionHandlingService {
    private List<ExceptionHandler> handlers = new ArrayList<>();
    private static ExceptionHandlingService exceptionHandlingService;

    private ExceptionHandlingService(){
        registerHandler(new LogExceptionHandler());
    }

    public void registerHandler(ExceptionHandler handler) {
        handlers.add(handler);
    }

    public void handleException(CustomException exception) {
        for (ExceptionHandler handler : handlers) {
            handler.handleException(exception);
        }
    }

    /**
     * 单例模式的获取状态方法
     * @return
     */
    public static ExceptionHandlingService getExceptionHandlingService() {
        if(exceptionHandlingService == null)
            exceptionHandlingService = new ExceptionHandlingService();
        return exceptionHandlingService;
    }
}

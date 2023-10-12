package com.xyl.sqlmanager.exception;

import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlingService {
    private List<ExceptionHandler> handlers = new ArrayList<>();

    public void registerHandler(ExceptionHandler handler) {
        handlers.add(handler);
    }

    public void handleException(Exception exception) {
        for (ExceptionHandler handler : handlers) {
            handler.handleException(exception);
        }
    }
}

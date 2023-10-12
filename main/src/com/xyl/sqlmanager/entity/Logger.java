package com.xyl.sqlmanager.entity;

public interface Logger {
    default void info(String message) {
        log(LogLevel.INFO, message);
    }

    default void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    default void error(String message) {
        log(LogLevel.ERROR, message);
    }

    void log(LogLevel level, String message);
}
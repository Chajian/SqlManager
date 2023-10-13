package com.xyl.sqlmanager.exception;

import com.xyl.sqlmanager.entity.LogLevel;
import com.xyl.sqlmanager.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * log日志处理器
 */
public class LogExceptionHandler implements ExceptionHandler{

    List<String> logInfo = new ArrayList<>();
    @Override
    public void handleException(CustomException exception) {
        String time = DataUtils.timestameToString(System.currentTimeMillis());
        String out = time+ LogLevel.ERROR.name() +":\n"+exception.getCode()+":"+exception.getMessage();
        logInfo.add(out);
        System.out.println(out);
    }
}

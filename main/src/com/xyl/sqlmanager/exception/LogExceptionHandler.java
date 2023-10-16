package com.xyl.sqlmanager.exception;

import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.entity.LogLevel;
import com.xyl.sqlmanager.util.DataUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 报错日志处理器
 */
public class LogExceptionHandler implements ExceptionHandler{

    List<String> logInfo = new ArrayList<>();
    @Override
    public void handleException(CustomException exception) {
        String time = DataUtils.timestameToString(System.currentTimeMillis());
        String out = time+ LogLevel.ERROR.name() +":\n"+exception.getCode()+":"+exception.getMessage();
        logInfo.add(out);
        JOptionPane.showMessageDialog(SqlManagerContext.getSqlManagerContext().getCurFrame(), out, "异常提示", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(out);
    }
}

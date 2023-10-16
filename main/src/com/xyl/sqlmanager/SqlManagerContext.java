package com.xyl.sqlmanager;

import javax.swing.*;

/**
 * SqlManager上下文
 */
public class SqlManagerContext {
    /*当前JFrame*/
    JFrame curFrame;

    private static SqlManagerContext sqlManagerContext;
    private SqlManagerContext() {
    }

    public static SqlManagerContext getSqlManagerContext(){
        if(sqlManagerContext==null){
            sqlManagerContext = new SqlManagerContext();
        }
        return sqlManagerContext;
    }

    public JFrame getCurFrame() {
        return curFrame;
    }

    public void setCurFrame(JFrame curFrame) {
        this.curFrame = curFrame;
    }
}

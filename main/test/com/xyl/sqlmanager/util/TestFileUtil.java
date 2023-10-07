package com.xyl.sqlmanager.util;

import com.xyl.sqlmanager.entity.ConnectInfo;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class TestFileUtil {

    @Test
    public void testSave() throws InvocationTargetException, IllegalAccessException {
        FileUtil fileUtil = new FileUtil();
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setHost("localhost");
        connectInfo.setUser("root");
        connectInfo.setPass("123456");
        connectInfo.setPort("3306");
        connectInfo.setName("test连接");
        connectInfo.setVersion("5.x");
        connectInfo.setTimestamp(String.valueOf(new Date().getTime()));
        fileUtil.saveConnectInfo(connectInfo);
    }

}

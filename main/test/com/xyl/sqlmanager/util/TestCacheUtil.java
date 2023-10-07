package com.xyl.sqlmanager.util;

import com.xyl.sqlmanager.entity.ConnectInfo;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

public class TestCacheUtil {

    @Test
    public void testSave() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        CacheUtil cacheUtil = new CacheUtil();
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setHost("localhost");
        connectInfo.setUser("root");
        connectInfo.setPass("123456");
        connectInfo.setPort("3306");
        connectInfo.setName("test连接");
        connectInfo.setVersion("5.x");
        connectInfo.setTimestamp(String.valueOf(new Date().getTime()));
        cacheUtil.saveConnectInfo(connectInfo);
    }

    @Test
    public void getConnections(){
        CacheUtil cacheUtil = new CacheUtil();
        List<ConnectInfo> connectInfos = cacheUtil.getConnectInfos();
        for(ConnectInfo connectInfo:connectInfos){
            System.out.println(connectInfo.toString());
        }

    }

}

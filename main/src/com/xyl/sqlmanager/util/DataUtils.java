package com.xyl.sqlmanager.util;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具
 */
public class DataUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Date data;
    /**
     * 时间戳转String
     * @return
     */
    public static String timestameToString(long timestamp){
        data = new Date(timestamp);
        return sdf.format(data).toString();
    }


}

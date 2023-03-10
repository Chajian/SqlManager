package com.xyl.sqlmanager.util;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Mysql字符串生成工具
 */
public class SqlStringUtil {

    /**
     * 把集合转换成columns
     * @param list
     * @return
     */
    public static String toColumn(List<String> list){
        AtomicReference<String> sql = new AtomicReference<>("");
        list.stream()
                .forEach(
                        e->{
                            String temp = sql.get();
                            temp += " "+e.toString()+",";
                            sql.set(temp);
                        }
                );
        String strSql = sql.get();
        strSql = strSql.substring(0,strSql.length()-1);
        return strSql;
    }

    public static String toAndWhere(Map<String,String> where,List<Boolean> andOr){
        AtomicReference<String> sql = new AtomicReference<>("");
        //提取Sql指令
        AtomicInteger on = new AtomicInteger();
        where.keySet().stream()
                .map(e->{
                    String strAndOr = andOr.get(on.get())?"AND":"OR";
                    on.set(on.get()+1);
                    return where.get(e.toString())==null?" "+e.toString()+" = "+where.get(e.toString())+" "+strAndOr:" "+e.toString()+" = '"+where.get(e.toString())+"' "+strAndOr;
                })
                .forEach(e->{
                    String temp = sql.get();
                    temp +=e.toString();
                    sql.set(temp);
                });
        String strSql = sql.get();
        strSql = strSql.substring(0,strSql.length()-3);
        return strSql;
    }

    /**
     * where过滤Null
     * @param where
     * @param andOr
     * @return
     */
    public static String toAndWhereNotNull(Map<String,String> where,List<Boolean> andOr){
        AtomicReference<String> sql = new AtomicReference<>("");
        //提取Sql指令
        AtomicInteger on = new AtomicInteger();
        on.set(0);
        where.keySet().stream()
                .filter(e->{
//                    boolean isNull = where.get(e.toString())==null;
//                    if(isNull)
//                        andOr.remove(on.get());
//                    on.set(on.get()+1);
                    return where.get(e.toString())!=null;
                })//过滤空
                .map(e->{
                    String strAndOr = andOr.get(on.get())?"AND":"OR";
                    on.set(on.get()+1);
                    return " "+e.toString()+" = '"+where.get(e.toString())+"' "+strAndOr;
                })
                .forEach(e->{
                    String temp = sql.get();
                    temp +=e.toString();
                    sql.set(temp);
                });
        String strSql = sql.get();
        strSql = strSql.substring(0,strSql.length()-3);
        return strSql;
    }

    public static String toUpdate(Map<String,String> map){
        AtomicReference<String> sql = new AtomicReference<>("");
        map.keySet().stream()
                .map(
                        e->{
                            return " "+e.toString()+" = '"+map.get(e.toString()) + "' and";
                        }
                )
                .forEach(
                        e->{
                            String temp = sql.get();
                            temp += e;
                            sql.set(temp);
                        }
                );
        String strSql = sql.get();
        strSql = strSql.substring(0,strSql.length()-3);
        return strSql;
    }

    public static String toInsert(List<String> values){
        String sql = values.get(0)==null?" values("+values.get(0):" values('"+values.get(0)+"'";
        //提取Sql指令
        for(int i = 1 ; i< values.size();i++){
            if(values.get(i)!=null)
                sql += ", '" + String.valueOf(values.get(i))+"'";
            else
                sql += ", " + String.valueOf(values.get(i));
        }
        sql+=")";
        return sql;
    }


}

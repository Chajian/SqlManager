package com.xyl.sqlmanager.util;

import com.xyl.sqlmanager.entity.ConnectInfo;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件读写
 */
public class FileUtil {
    String path = "aa.jj";

    public boolean writeByte(byte[] context,File file){
        try {

            FileOutputStream fileInputStream = new FileOutputStream(file,file.exists());
            fileInputStream.write(context);
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List readBytes(File file) throws IOException {
        List<String> list = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[192];
        while(fileInputStream.read(buffer)!=-1){
            String info = buffer.toString();
            list.add(info);
        }
        return list;
    }

    /**
     *
     * @param info 字符串
     * @param button byte存放池
     * @param start 开始存放位置
     */
    public void stringToBytes32(String info,byte[] button,int start){
        byte[] context = info.getBytes();
        for(int i = 0 ; i < context.length&&i<32;i++){
            button[start++]=context[i];
        }
    }

    public void intToBytes32(int value,byte[] buttom,int start) {
        // Convert the integer to bytes using bit manipulation
        buttom[start++] = (byte) (value >> 24);
        buttom[start++] = (byte) (value >> 16);
        buttom[start++] = (byte) (value >> 8);
        buttom[start++] = (byte) value;
    }

    /**
     * 保存远程连接信息
     */
    public void saveConnectInfo(ConnectInfo connectInfo) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Field[] fields = ConnectInfo.class.getFields();
        List<Method> list = new ArrayList<>();
        for(Field field:fields) {
            String name = field.getName();
            name = "get"+(char)(name.charAt(0)-32)+name.substring(1,name.length());
            Method method = ConnectInfo.class.getMethod(name,null);
            list.add(method);
        }

        byte[] bytes = new byte[32*list.size()];
        for(int i = 0 ; i < list.size();i++){
            String info = (String) list.get(i).invoke(connectInfo,null);
            stringToBytes32(info,bytes,i*32);
        }
        File file = new File(path);
        writeByte(bytes,file);
    }

    /**
     * 获取远程连接信息
     * @return
     */
    public List<ConnectInfo> getConnectInfos(){
        List<ConnectInfo> connectInfos = new ArrayList<>();
        File file = new File(path);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[32];
            Field[] fields = ConnectInfo.class.getFields();
            while(fileInputStream.available()>0){
                ConnectInfo connectInfo = new ConnectInfo();
                for(int i = 0 ; i < fields.length&&fileInputStream.read(bytes)!=-1;i++) {
                    String value = new String(bytes).trim();
                    String name = fields[i].getName();
                    name = "set"+(char)(name.charAt(0)-32)+name.substring(1,name.length());
                    Method method = ConnectInfo.class.getMethod(name,String.class);
                    method.invoke(connectInfo,value);
                }
                connectInfos.add(connectInfo);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return connectInfos
                .stream()
                .sorted(Comparator.comparing(ConnectInfo::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

}

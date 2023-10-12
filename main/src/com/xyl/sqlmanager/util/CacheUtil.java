package com.xyl.sqlmanager.util;

import com.xyl.sqlmanager.entity.ConnectInfo;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件读写-缓存工具
 * @author YangLin
 * @date 2023/10/7 20:55
 */
public class CacheUtil {
    String path = "aa.jj";

    public CacheUtil(String path) {
        this.path = path;
    }

    public CacheUtil() {
    }

    /**
     * 把二进制流写入到文件中
     * @param context
     * @param file
     * @return
     */
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



    /**
     * 把字符串转换为byte数组
     * 固定大小为32,不足填0
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

    /**
     * 把int转换为byte数组
     * 固定大小为32,不足填0
     * @param value 值
     * @param buttom byte存放池
     * @param start 开始存放位置
     */
    public void intToBytes32(int value,byte[] buttom,int start) {
        // Convert the integer to bytes using bit manipulation
        buttom[start++] = (byte) (value >> 24);
        buttom[start++] = (byte) (value >> 16);
        buttom[start++] = (byte) (value >> 8);
        buttom[start++] = (byte) value;
    }

    /**
     * 保存远程连接信息到缓存文件中
     * @param connectInfo 被保存对象
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
            stringToBytes32(info==null?"":info,bytes,i*32);
        }
        File file = new File(path);
        writeByte(bytes,file);
    }

    /**
     * 获取远程连接信息从缓存文件中
     * 按照时间顺序降序排列
     * @return list
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

    /**
     * 获取远程连接信息从缓存文件中
     * 按照时间顺序降序排列
     * @return list
     */
    public byte[] getConnectInfoBytes(){
        byte[] bytes = null;
        File file = new File(path);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            return bytes;
        }
    }

    /**
     * 更新ConnectInfo
     * @param connectInfo
     * @param index
     * @return
     */
    public boolean updateConnectInfo(ConnectInfo connectInfo,int index){
        File file = new File(path);
        try {
            byte[] allbytes = getConnectInfoBytes();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int skip = (index * ConnectInfo.class.getFields().length)*32;
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
                stringToBytes32(info==null?"":info,bytes,i*32);
            }
            for(int i = 0;i<bytes.length;i++){
                allbytes[skip+i] = bytes[i];
            }
            fileOutputStream.write(allbytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 获取ConnectInfo通过下标
     * @param index
     * @return
     */
    public ConnectInfo getConnectInfoByIndex(int index){
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int skip = (index * ConnectInfo.class.getFields().length)*32;
            fileInputStream.skip(skip);
            ConnectInfo connectInfo = new ConnectInfo();
            byte[] bytes = new byte[32];
            Field[] fields = ConnectInfo.class.getFields();
            for(int i = 0 ; i < fields.length&&fileInputStream.read(bytes)!=-1;i++) {
                String value = new String(bytes).trim();
                String name = fields[i].getName();
                name = "set"+(char)(name.charAt(0)-32)+name.substring(1,name.length());
                Method method = ConnectInfo.class.getMethod(name,String.class);
                method.invoke(connectInfo,value);
            }
            return connectInfo;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

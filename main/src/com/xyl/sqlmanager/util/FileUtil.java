package com.xyl.sqlmanager.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读写
 */
public class FileUtil {

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

}

package com.xyl.sqlmanager.gui.panel;

import com.mysql.cj.util.StringUtils;
import com.xyl.sqlmanager.entity.ConnectInfo;
import com.xyl.sqlmanager.util.CacheUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MysqlCard extends Panel{
    JTextField ip,port,user;
    JPasswordField pass;
    JRadioButton version5,version8;
    ButtonGroup cg = new ButtonGroup();
    //连接信息缓存下拉框
    JComboBox hostInfos;
    /*缓存工具类*/
    CacheUtil cacheUtil;

    {
        hostInfos = new JComboBox();
        loadCacheHost("aa.jj");
        hostInfos.setSelectedIndex(-1);
        hostInfos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理选中项的逻辑
                ConnectInfo connectInfo = (ConnectInfo) hostInfos.getSelectedItem();
                if(!StringUtils.isNullOrEmpty(connectInfo.getHost())){
                    ip.setText(connectInfo.getHost());
                }
                if(!StringUtils.isNullOrEmpty(connectInfo.getPort())){
                    port.setText(connectInfo.getPort());
                }
                if(!StringUtils.isNullOrEmpty(connectInfo.getUser())){
                    user.setText(connectInfo.getUser());
                }
                if(!StringUtils.isNullOrEmpty(connectInfo.getPass())){
                    pass.setText(connectInfo.getPass());
                }
                revalidate();
            }
        });

        ip = new JTextField("localhost",30);
        port = new JTextField(30);
        user = new JTextField("root",30);
        pass = new JPasswordField("123456789",30);
        add(new JLabel("数据库连接信息"));
        add(hostInfos);
        add(new JLabel("IP"));
        add(ip);
        add(new JLabel("端口"));
        add(port);
        add(new JLabel("用户名"));
        add(user);
        add(new JLabel("密码"));
        add(pass);
        GridLayout gridLayout = new GridLayout(6,2);

        version5 = new JRadioButton("MYSQL5.x",false);
        version8 = new JRadioButton("MYSQL8.x",true);
        cg.add(version5);
        cg.add(version8);
        add(version5);
        add(version8);

        setLayout(gridLayout);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    /**
     * 加载缓存的主机信息
     * @param path 缓存存放路径
     */
    public void loadCacheHost(String path){
        cacheUtil = new CacheUtil(path);
        List<ConnectInfo> list = cacheUtil.getConnectInfos();
        for(ConnectInfo connectInfo:list){
            hostInfos.addItem(connectInfo);
        }
    }

    /**
     * 新增新的connectInfo
     * @param connectInfo
     */
    public void addConnectInfo(ConnectInfo connectInfo) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        cacheUtil.saveConnectInfo(connectInfo);
    }

    public JComboBox getHostInfos() {
        return hostInfos;
    }

    public void setHostInfos(JComboBox hostInfos) {
        this.hostInfos = hostInfos;
    }

    public JTextField getIp() {
        return ip;
    }

    public void setIp(JTextField ip) {
        this.ip = ip;
    }

    public JTextField getPort() {
        return port;
    }

    public void setPort(JTextField port) {
        this.port = port;
    }

    public JTextField getUser() {
        return user;
    }

    public void setUser(JTextField user) {
        this.user = user;
    }

    public JPasswordField getPass() {
        return pass;
    }

    public void setPass(JPasswordField pass) {
        this.pass = pass;
    }

    public JRadioButton getVersion5() {
        return version5;
    }

    public void setVersion5(JRadioButton version5) {
        this.version5 = version5;
    }

    public JRadioButton getVersion8() {
        return version8;
    }

    public void setVersion8(JRadioButton version8) {
        this.version8 = version8;
    }

    public CacheUtil getCacheUtil() {
        return cacheUtil;
    }
}
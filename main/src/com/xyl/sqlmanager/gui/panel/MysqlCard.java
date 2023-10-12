package com.xyl.sqlmanager.gui.panel;

import javax.swing.*;
import java.awt.*;

public class MysqlCard extends Panel{
    JTextField ip,port,user;
    JPasswordField pass;
    JRadioButton version5,version8;
    ButtonGroup cg = new ButtonGroup();

    {
        ip = new JTextField("localhost",30);
        port = new JTextField(30);
        user = new JTextField("root",30);
        pass = new JPasswordField("123456789",30);
        add(new JLabel("IP"));
        add(ip);
        add(new JLabel("端口"));
        add(port);
        add(new JLabel("用户名"));
        add(user);
        add(new JLabel("密码"));
        add(pass);
        GridLayout gridLayout = new GridLayout(5,2);

        version5 = new JRadioButton("MYSQL5.x",false);
        version8 = new JRadioButton("MYSQL8.x",true);
        cg.add(version5);
        cg.add(version8);
        add(version5);
        add(version8);

        setLayout(gridLayout);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
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
}
package com.xyl.sqlmanager.gui;

import com.xyl.sqlmanager.MySqlDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;
import java.nio.charset.Charset;
import java.sql.SQLException;

/**
 * 连接数据库UI
 */
public class MyConnection extends JFrame {

    JTextField ip,port,user;
    JPasswordField pass;
    ButtonGroup cg = new ButtonGroup();
    ButtonGroup choice = new ButtonGroup();
    JButton login,exit;
    JRadioButton version5,version8;
    MySqlDriver mySqlDriver;
    Panel mysqlPanel,sqlitePanel,choosePanel,mysqlVersionPanel,connectionPanel;



    public MyConnection(){
        super("数据库连接工具");
        setLayout(new GridLayout(4,1));

        //选择sql数据库
        choosePanel = new Panel();
        JLabel choonseTitle = new JLabel("数据库类型");
        JRadioButton mysql = new JRadioButton("MYSQL",true);
        JRadioButton sqlite = new JRadioButton("SQLITE",false);
        sqlite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchSqlite();
            }
        });
        mysql.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchMysql();
            }
        });

        choice.add(mysql);
        choice.add(sqlite);
        choosePanel.add(choonseTitle);
        choosePanel.add(mysql);
        choosePanel.add(sqlite);

        //mysql
        mysqlPanel = new Panel();
        ip = new JTextField("localhost",30);
        port = new JTextField(30);
        user = new JTextField("root",30);
        pass = new JPasswordField("123456789",30);
        mysqlPanel.add(new JLabel("IP"));
        mysqlPanel.add(ip);
        mysqlPanel.add(new JLabel("端口"));
        mysqlPanel.add(port);
        mysqlPanel.add(new JLabel("用户名"));
        mysqlPanel.add(user);
        mysqlPanel.add(new JLabel("密码"));
        mysqlPanel.add(pass);
        mysqlPanel.setLayout(new GridLayout(4,2));
        mysqlPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        mysqlVersionPanel = new Panel();

        version5 = new JRadioButton("MYSQL5.x",false);
        version8 = new JRadioButton("MYSQL8.x",true);
        cg.add(version5);
        cg.add(version8);
        mysqlVersionPanel.add(version5);
        mysqlVersionPanel.add(version8);

        connectionPanel = new Panel();
        login = new JButton("连接");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strUser = user.getText().toString();
                String strPass = pass.getText().toString();
                String strIp = ip.getText().toString();
                String strPort = port.getText().toString();
                int version = 8;
                if(version5.isSelected()){
                    version = 5;
                }

                try {
                    mySqlDriver = new MySqlDriver(strUser,strPass,strIp,strPort,version);
                    if(mySqlDriver.getConnection()!=null)
                    {
                        JOptionPane.showMessageDialog(null,"连接成功");
                        new MainPanel(mySqlDriver);
                        dispose();
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        exit = new JButton("退出");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        connectionPanel.add(login);
        connectionPanel.add(exit);

        sqlitePanel = new Panel();
        //sqlite
        JButton openSqliteFile = new JButton("选择sqlite文件");
        openSqliteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JFileChooser jfilechooser = new JFileChooser();
//                int returnVal = jfilechooser.showOpenDialog(null);
//                if (returnVal == JFileChooser.APPROVE_OPTION) {
//                    textField.setText(jfilechooser.getSelectedFile().getAbsolutePath());
//                    openFile();
//                }
            }
        });
        sqlitePanel.add(openSqliteFile);

        //选择版本
        add(choosePanel);

        //默认mysql
        add(mysqlPanel);
        add(mysqlVersionPanel);


        //连接
        add(connectionPanel);


        Charset utf8Charset = Charset.forName("UTF-8");
        Font font = new Font("宋体", Font.PLAIN, 12);
        setFont(font);
        setVisible(true);
        setSize(1000,500);
    }

    public static void main(String[] args) {
        try {
            new MyConnection();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void switchSqlite(){
        this.remove(mysqlPanel);
        this.remove(mysqlVersionPanel);
        this.add(sqlitePanel);
    }

    public void switchMysql(){
        this.remove(sqlitePanel);
        this.add(mysqlPanel);
        this.add(mysqlVersionPanel);
    }

}

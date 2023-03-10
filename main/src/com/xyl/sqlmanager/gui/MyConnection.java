package com.xyl.sqlmanager.gui;

import com.xyl.sqlmanager.MySqlDriver;
import com.xyl.sqlmanager.SqliteDriver;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 连接数据库UI
 */
public class MyConnection extends JFrame {

    public static boolean isEnableTimes = false;

    JTextField ip,port,user;
    JPasswordField pass;
    ButtonGroup cg = new ButtonGroup();
    ButtonGroup choice = new ButtonGroup();
    JButton login,exit,openSqliteFile;
    JRadioButton version5,version8;
    MySqlDriver mySqlDriver;
    Panel mysqlPanel,sqlitePanel,choosePanel,mysqlVersionPanel,connectionPanel;
    File sqliteFile = null;


    public MyConnection(){
        super("数据库连接工具");
        setLayout(new GridLayout(5,1));

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
                if(mysql.isSelected()) {
                    String strUser = user.getText().toString();
                    String strPass = pass.getText().toString();
                    String strIp = ip.getText().toString();
                    String strPort = port.getText().toString();
                    int version = 8;
                    if (version5.isSelected()) {
                        version = 5;
                    }

                    try {
                        mySqlDriver = new MySqlDriver(strUser, strPass, strIp, strPort, version);
                        if (mySqlDriver.getConnection() != null) {
                            JOptionPane.showMessageDialog(null, "连接成功");
                            new MainPanel(mySqlDriver);
                            dispose();
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                else if(sqlite.isSelected()){
                    try {
                        SqliteDriver sqliteDriver = new SqliteDriver(sqliteFile);
                        new MainPanel(sqliteDriver);
                        dispose();
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                    }
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
        openSqliteFile = new JButton("选择sqlite文件");
        openSqliteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfilechooser = new JFileChooser();
                FileFilter filter = new DBFileFilter();
                jfilechooser.setFileFilter(filter);
                int returnVal = jfilechooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    sqliteFile = jfilechooser.getSelectedFile();
                }
            }
        });
        sqlitePanel.add(new JLabel("选择Sqlite的db文件"));
        sqlitePanel.add(openSqliteFile);
        sqlitePanel.setVisible(true);

        //选择版本
        add(choosePanel);
        add(mysqlPanel);
        add(mysqlVersionPanel);
        add(sqlitePanel);
        add(connectionPanel);

        switchMysql();

        Charset utf8Charset = Charset.forName("UTF-8");
        Font font = new Font("宋体", Font.PLAIN, 12);
        setFont(font);
        setVisible(true);
        setSize(1000,500);
    }

    public static void main(String[] args) {
        // 获取GraphicsEnvironment对象
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        Arrays.stream(ge.getAvailableFontFamilyNames())
                .forEach(
                        e->{
                            if(e.equals("Times")){//判断是否加载字体
                                isEnableTimes = true;
                            }
                    }
                );


        try {
            if(!isEnableTimes){
                // 加载“java”字体
                Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
                // 注册“java”字体
                ge.registerFont(font);
            }
            new MyConnection();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void switchSqlite(){
        mysqlPanel.setVisible(false);
        mysqlVersionPanel.setVisible(false);
        sqlitePanel.setVisible(true);
    }

    public void switchMysql(){
        sqlitePanel.setVisible(false);
        mysqlPanel.setVisible(true);
        mysqlVersionPanel.setVisible(true);
    }

}

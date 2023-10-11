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
    /**
     * cardLayout布局
     * 采用卡片布局切换sqlite和mysqlui
     */
    Panel connectInfoPanel;
    File sqliteFile = null;
    JRadioButton mysql,sqlite;
    /**
     * 卡片布局，切换sqlite和mysql
     */
    CardLayout cardLayout;


    public MyConnection(){
        super("数据库连接工具");
        setLayout(new GridLayout(3,1));
        connectionPanel = new ConnectCard();
        connectInfoPanel = new Panel();
        cardLayout = new CardLayout();
        connectInfoPanel.setLayout(cardLayout);


        //选择sql数据库点击事件
        choosePanel = new Panel();
        JLabel choonseTitle = new JLabel("数据库类型");
        mysql = new JRadioButton("MYSQL",true);
        sqlite = new JRadioButton("SQLITE",false);
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


        mysqlPanel = new MysqlCard();
        connectInfoPanel.add(mysqlPanel,"mysql");
        sqlitePanel = new SqliteCard();
        connectInfoPanel.add(sqlitePanel,"sqlite");

        //选择版本
        add(choosePanel);
        add(connectInfoPanel);
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
        cardLayout.show(connectInfoPanel,"sqlite");
    }

    public void switchMysql(){
        cardLayout.show(connectInfoPanel,"mysql");
    }

    /**
     * xxxCard 存放xxxPanel的组件和相应的事件
     */
    class MysqlCard extends Panel{

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
//            mysqlVersionPanel = new Panel();

            version5 = new JRadioButton("MYSQL5.x",false);
            version8 = new JRadioButton("MYSQL8.x",true);
            cg.add(version5);
            cg.add(version8);
            add(version5);
            add(version8);

            setLayout(gridLayout);
            this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
    }
    class SqliteCard extends Panel{
        {
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
            add(new JLabel("选择Sqlite的db文件"));
            add(openSqliteFile);
            setVisible(true);
        }
    }

    class ConnectCard extends Panel{
        {
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
            add(login);
            add(exit);
        }
    }

}

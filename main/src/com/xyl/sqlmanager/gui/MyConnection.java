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


    MySqlDriver mySqlDriver;
    ConnectInfoCard connectInfoCard;
    ConnectCard connectCard;
    MysqlCard mysqlCard;
    SqliteCard sqliteCard;
    ChooseCard chooseCard;



    public MyConnection(){
        super("数据库连接工具");
        setLayout(new GridLayout(3,1));
        connectCard = new ConnectCard();
        chooseCard = new ChooseCard();
        connectInfoCard = new ConnectInfoCard();



        //选择版本
        add(chooseCard);
        add(connectInfoCard);
        add(connectCard);
        connectInfoCard.switchMysql();

        Charset utf8Charset = Charset.forName("UTF-8");
        Font font = new Font("宋体", Font.PLAIN, 12);
        setFont(font);
        setVisible(true);
        setSize(1000,500);
    }

    public static void main(String[] args) {
        // 获取GraphicsEnvironment对象
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        //修复mac加载字体失败导致的闪崩问题
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


    /**
     * xxxCard 存放xxxPanel的组件和相应的事件
     */

    class ConnectInfoCard extends Panel{
        /**
         * cardLayout布局
         * 采用卡片布局切换sqlite和mysqlui
         */
        CardLayout cardLayout;
        {
            cardLayout = new CardLayout();
            setLayout(cardLayout);
            mysqlCard = new MysqlCard();
            add(mysqlCard,"mysql");
            sqliteCard = new SqliteCard();
            add(sqliteCard,"sqlite");
        }

        public void switchSqlite(){
            cardLayout.show(this,"sqlite");
        }

        public void switchMysql(){
            cardLayout.show(this,"mysql");
        }

    }
    class MysqlCard extends Panel{
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
    class SqliteCard extends Panel{
        JButton openSqliteFile;
        File sqliteFile = null;

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

        public File getSqliteFile() {
            return sqliteFile;
        }

        public void setSqliteFile(File sqliteFile) {
            this.sqliteFile = sqliteFile;
        }
    }

    class ConnectCard extends Panel{
        JButton login,exit;
        {
            login = new JButton("连接");
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(chooseCard.getMysql().isSelected()) {
                        String strUser = mysqlCard.getUser().getText().toString();
                        String strPass = mysqlCard.getPass().getText().toString();
                        String strIp = mysqlCard.getIp().getText().toString();
                        String strPort = mysqlCard.getPort().getText().toString();
                        int version = 8;
                        if (mysqlCard.getVersion5().isSelected()) {
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
                    else if(chooseCard.getSqlite().isSelected()){
                        try {
                            SqliteDriver sqliteDriver = new SqliteDriver(sqliteCard.getSqliteFile());
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
    class ChooseCard extends Panel{
        JRadioButton mysql,sqlite;
        ButtonGroup choice = new ButtonGroup();

        {
            //选择sql数据库点击事件
            JLabel choonseTitle = new JLabel("数据库类型");
            mysql = new JRadioButton("MYSQL",true);
            sqlite = new JRadioButton("SQLITE",false);
            sqlite.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    connectInfoCard.switchSqlite();
                }
            });
            mysql.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    connectInfoCard.switchMysql();
                }
            });

            choice.add(mysql);
            choice.add(sqlite);
            add(choonseTitle);
            add(mysql);
            add(sqlite);
        }

        public JRadioButton getMysql() {
            return mysql;
        }

        public void setMysql(JRadioButton mysql) {
            this.mysql = mysql;
        }

        public JRadioButton getSqlite() {
            return sqlite;
        }

        public void setSqlite(JRadioButton sqlite) {
            this.sqlite = sqlite;
        }
    }

}

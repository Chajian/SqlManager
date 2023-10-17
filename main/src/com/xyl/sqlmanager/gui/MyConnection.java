package com.xyl.sqlmanager.gui;

import com.xyl.sqlmanager.MySqlDriver;
import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.SqliteDriver;
import com.xyl.sqlmanager.entity.ConnectInfo;
import com.xyl.sqlmanager.exception.CustomException;
import com.xyl.sqlmanager.exception.ResponseEnum;
import com.xyl.sqlmanager.gui.main.MainPanel;
import com.xyl.sqlmanager.gui.panel.ConnectInfoCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
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
        SqlManagerContext.getSqlManagerContext().setCurFrame(this);
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


    class ConnectCard extends Panel{
        JButton login,exit;
        {
            login = new JButton("连接");
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(chooseCard.getMysql().isSelected()) {
                        String strUser = connectInfoCard.getMysqlCard().getUser().getText().toString();
                        String strPass = connectInfoCard.getMysqlCard().getPass().getText().toString();
                        String strIp = connectInfoCard.getMysqlCard().getIp().getText().toString();
                        String strPort = connectInfoCard.getMysqlCard().getPort().getText().toString();
                        int version = 8;
                        if (connectInfoCard.getMysqlCard().getVersion5().isSelected()) {
                            version = 5;
                        }

                        try {
                            mySqlDriver = new MySqlDriver(strUser, strPass, strIp, strPort, version);
                            if (mySqlDriver.getConnection() != null) {
                                JOptionPane.showMessageDialog(null, "连接成功");
                                //缓存连接信息
                                if(connectInfoCard.getMysqlCard().getHostInfos().getSelectedIndex()==-1){
                                    ConnectInfo connectInfo = new ConnectInfo();
                                    connectInfo.setName(String.valueOf(System.currentTimeMillis()));
                                    connectInfo.setHost(strIp);
                                    connectInfo.setPort(strPort);
                                    connectInfo.setUser(strUser);
                                    connectInfo.setPass(strPass);
                                    connectInfo.setTimestamp(String.valueOf(System.currentTimeMillis()));
                                    connectInfo.setVersion(String.valueOf(version));
                                    connectInfoCard.getMysqlCard().getCacheUtil().saveConnectInfo(connectInfo);
                                }
                                else{
                                    ConnectInfo connectInfo = (ConnectInfo) connectInfoCard.getMysqlCard().getHostInfos().getSelectedItem();
                                    connectInfo.setHost(strIp);
                                    connectInfo.setPort(strPort);
                                    connectInfo.setUser(strUser);
                                    connectInfo.setPass(strPass);
                                    connectInfo.setTimestamp(String.valueOf(System.currentTimeMillis()));
                                    connectInfoCard.getMysqlCard().getCacheUtil().updateConnectInfo(connectInfo,connectInfoCard.getMysqlCard().getHostInfos().getSelectedIndex());
                                }

                                new MainPanel(mySqlDriver);
                                dispose();
                            }
                        } catch (ClassNotFoundException | SQLException | InvocationTargetException |
                                 IllegalAccessException | NoSuchMethodException | CustomException ex) {
                            //统一异常处理
                            throw new CustomException(ResponseEnum.DRIVE_SQL_EXCEPTION.getCode(),ResponseEnum.DRIVE_SQL_EXCEPTION.getMes()+ex.getMessage());
                        }
                    }
                    else if(chooseCard.getSqlite().isSelected()){
                        try {
                            SqliteDriver sqliteDriver = new SqliteDriver(connectInfoCard.getSqliteCard().getSqliteFile());
                            new MainPanel(sqliteDriver);
                            dispose();
                        } catch (ClassNotFoundException | SQLException ex) {
                            //统一异常处理
                            throw new CustomException(ResponseEnum.DRIVE_SQL_EXCEPTION.getCode(),ResponseEnum.DRIVE_SQL_EXCEPTION.getMes()+ex.getMessage());
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

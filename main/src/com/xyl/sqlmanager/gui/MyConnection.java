package com.xyl.sqlmanager.gui;

import com.xyl.sqlmanager.MySqlDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * 连接数据库UI
 */
public class MyConnection extends JFrame {

    JTextField ip,port,user;
    JPasswordField pass;
    ButtonGroup cg = new ButtonGroup();
    JButton login,exit;
    JRadioButton version5,version8;
    MySqlDriver mySqlDriver;



    public MyConnection() throws HeadlessException {
        super("数据库连接工具");
        setLayout(new GridLayout(3,1));

        Panel panel = new Panel();
        ip = new JTextField("localhost",30);
        port = new JTextField(30);
        user = new JTextField("root",30);
        pass = new JPasswordField("123456789",30);
        panel.add(new JLabel("IP"));
        panel.add(ip);
        panel.add(new JLabel("端口"));
        panel.add(port);
        panel.add(new JLabel("用户名"));
        panel.add(user);
        panel.add(new JLabel("密码"));
        panel.add(pass);
        panel.setLayout(new GridLayout(4,2));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        Panel panel1 = new Panel();

        version5 = new JRadioButton("MYSQL5.x",false);
        version8 = new JRadioButton("MYSQL8.x",true);
        cg.add(version5);
        cg.add(version8);
        panel1.add(version5);
        panel1.add(version8);

        Panel panel2 = new Panel();
        login = new JButton("登录");
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
                    mySqlDriver = new MySqlDriver(strUser,strPass,"",strIp,version);
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
            }
        });
        panel2.add(login);
        panel2.add(exit);


        add(panel);
        add(panel1);
        add(panel2);

        Font font = new Font("宋体", Font.PLAIN, 12);
        setFont(font);
        setVisible(true);
        setSize(1000,500);
    }

    public static void main(String[] args) {
        new MyConnection();
    }
}

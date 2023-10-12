package com.xyl.sqlmanager.ui;

import com.xyl.sqlmanager.MySqlDriver;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;

public class GridLayoutTest {
    static class testGui extends JFrame {
        JTextField ip,port,user;
        JPasswordField pass;
        ButtonGroup cg = new ButtonGroup();
        ButtonGroup choice = new ButtonGroup();
        JButton login,exit,openSqliteFile;
        JRadioButton version5,version8;
        MySqlDriver mySqlDriver;
        Panel mysqlPanel,sqlitePanel,choosePanel,mysqlVersionPanel,connectionPanel;
        File sqliteFile = null;

        {
            mysqlPanel = new Panel();

            GridBagLayout gridLayout = new GridBagLayout();
            mysqlPanel.setLayout(gridLayout);
            //mysql
            ip = new JTextField("localhost",30);
            port = new JTextField(30);
            user = new JTextField("root",30);
            pass = new JPasswordField("123456789",30);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 500; // 组件的列索引
            constraints.gridy = 200; // 组件的行索引
            constraints.gridwidth = 1; // 组件的宽度
            constraints.gridheight = 1; // 组件的高度
            constraints.weightx = 1; // 组件在水平方向上的拉伸权重
            constraints.weighty = 1; // 组件在垂直方向上的拉伸权重

            mysqlPanel.add(new JLabel("IP"),constraints);
            mysqlPanel.add(ip);
            mysqlPanel.add(new JLabel("端口"));
            mysqlPanel.add(port);
            mysqlPanel.add(new JLabel("用户名"));
            mysqlPanel.add(user);
            mysqlPanel.add(new JLabel("密码"));
            mysqlPanel.add(pass);

            testLayout();

            this.add(mysqlPanel);
            Charset utf8Charset = Charset.forName("UTF-8");
            Font font = new Font("宋体", Font.PLAIN, 12);
            setFont(font);
            setVisible(true);
            setSize(1000,500);
        }

        public void testLayout(){

        }
    }

    @Test
    public void start(){

    }

    public static void main(String[] args) {
        testGui testGui = new testGui();


    }

}

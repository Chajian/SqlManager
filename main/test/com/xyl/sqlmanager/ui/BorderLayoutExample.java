package com.xyl.sqlmanager.ui;

import javax.swing.*;
import java.awt.*;

public class BorderLayoutExample extends JFrame {
    public BorderLayoutExample() {
        setTitle("BorderLayout Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout bagLayout = new GridBagLayout();
        bagLayout.rowHeights = new int[]{8,1};
        bagLayout.rowWeights = new double[]{0.2,0.6,0.2};

        setLayout(bagLayout);

        // 创建组件
        JButton button1 = new JButton("Button 1");
        JButton button2 = new JButton("Button 2");
        JButton button3 = new JButton("Button 3");
        JButton button4 = new JButton("Button 4");
        JButton button5 = new JButton("Button 5");

        // 设置GridBagConstraints的属性
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;


        // 添加组件到容器
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        add(button1, constraints);



        constraints.gridwidth = 1;
        constraints.gridheight = 3;
        constraints.gridx = 0;
        constraints.gridy = 1;

        add(button2, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        add(button3, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;

        add(button4, constraints);


        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        add(button5, constraints);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BorderLayoutExample();
        });
    }
}

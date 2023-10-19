package com.xyl.sqlmanager.gui.main;

import com.xyl.sqlmanager.BaseSqlDriver;
import com.xyl.sqlmanager.MySqlDriver;
import com.xyl.sqlmanager.SqlManagerContext;
import com.xyl.sqlmanager.exception.CustomException;
import com.xyl.sqlmanager.exception.ResponseEnum;
import com.xyl.sqlmanager.util.TableHandler;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class MainPanel extends JFrame {

    String dbType = "Mysql";


    BaseSqlDriver mySqlDriver;
    Connection connection;
    String seletedDb,seletedTable;


    TableCard tableCard;
    TreeDirectoryCard treeDirectory;
    MenuCard jMenuBar;
    InputCard inputCard;

    public MainPanel(BaseSqlDriver mySqlDriver) throws HeadlessException, SQLException {
        // 创建根节点
        super("数据库");
        SqlManagerContext.getSqlManagerContext().setCurFrame(this);
        setLayout(new BorderLayout());
        this.mySqlDriver = mySqlDriver;
        treeDirectory = new TreeDirectoryCard();
        tableCard = new TableCard();
        jMenuBar = new MenuCard();
        inputCard = new InputCard();

        //添加菜单栏
        setJMenuBar(jMenuBar);
        add(jMenuBar,BorderLayout.NORTH);
        add(treeDirectory,BorderLayout.WEST);
        // 将滚动面板添加到窗体中央
        this.add(tableCard.spTable, BorderLayout.CENTER);
        add(inputCard,BorderLayout.SOUTH);
        // 设定窗口大小
        this.setSize(1200, 400);
        // 设定窗口左上角坐标（X轴200像素，Y轴100像素）
        this.setLocation(300, 200);
        // 设定窗口默认关闭方式为退出应用程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口可视（显示）
        this.setVisible(true);
    }

    public String getSeletedTable() {
        return seletedTable;
    }

    public BaseSqlDriver getMySqlDriver() {
        return mySqlDriver;
    }

    public String getSeletedDb() {
        return seletedDb;
    }

    public Connection getConnection() {
        return connection;
    }

    public TableCard getTableCard() {
        return tableCard;
    }

    public MenuCard getjMenuBar() {
        return jMenuBar;
    }

    //目录树面板
    class TreeDirectoryCard extends Panel implements TreeSelectionListener{
        JTree leftProject;
        DefaultTreeModel treeModel;
        DefaultMutableTreeNode root;
        /**
         * tree点击事件
         * @param e
         */
        @Override
        public void valueChanged(TreeSelectionEvent e)  {
            TreePath path = leftProject.getSelectionPath();
            if (path == null)
                return;
            if (path.getPathCount() == 2) {//点击数据库更新JTree组件ui
                DefaultMutableTreeNode treePath = (DefaultMutableTreeNode) path.getPath()[1];
                seletedDb = treePath.getUserObject().toString();
                mySqlDriver.useDataBase(connection, seletedDb);
            }
            if (path.getPathCount() == 3) {//点击表单更新JTableUI
                tableCard.setEnable(true);//开启表格监听
                jMenuBar.insert.setEnabled(true);//启用插入功能
                jMenuBar.delete.setEnabled(true);//启用删除功能
                DefaultMutableTreeNode treePath = (DefaultMutableTreeNode) path.getPath()[2];
                seletedTable = treePath.getUserObject().toString();
                List<String> columns = mySqlDriver.showTableColumnNames(connection,seletedTable);
                List<Map<String,String>> lists = mySqlDriver.select(connection,seletedTable,null,columns);
                //更新表格UI
                TableHandler.updateTableColumn(tableCard.table,tableCard.tableModel,columns,lists);
            }
        }
        public TreeDirectoryCard() throws SQLException {
            //panel存左侧的树
            root = new DefaultMutableTreeNode(dbType+"数据库");
            treeModel = new DefaultTreeModel(root);
            leftProject = new JTree(treeModel);
            leftProject.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            //获取所有数据库
            List<String> dbs = mySqlDriver.showDatabases(mySqlDriver.getConnection());
            //插入数据库node
            dbs.stream()
                    .forEach(e->{
                        insertDbNode(e.toString());
                        //插入表单node
                        try {
                            connection = mySqlDriver.getConnection();
                            mySqlDriver.useDataBase(connection,e);
                            List<String> tables = mySqlDriver.showTables(connection);
                            tables.stream()
                                    .forEach(e1->{
                                        insertTable(e.toString(),e1.toString());
                                    });
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    });
            // 在面板的左侧放置树
            leftProject.addTreeSelectionListener(this);
            add(new JScrollPane(leftProject));
        }
        /**
         * 插入表节点
         */
        public void insertTable(String dbName,String tableName){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(tableName);
            Enumeration<TreeNode> childrens = root.children();
            while(childrens.hasMoreElements()){
                DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) childrens.nextElement();
                if(node1.getUserObject().equals(dbName)){
                    treeModel.insertNodeInto(node,node1,node1.getChildCount());
                }
            }
        }
        /**
         * 插入库节点
         * @param dbName
         */
        public void insertDbNode(String dbName){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dbName);
            treeModel.insertNodeInto(node,root,root.getChildCount());
        }
    }






}


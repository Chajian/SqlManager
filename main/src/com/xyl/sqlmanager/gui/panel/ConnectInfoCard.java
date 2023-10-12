package com.xyl.sqlmanager.gui.panel;

import java.awt.*;

public class ConnectInfoCard extends Panel {
    /**
     * cardLayout布局
     * 采用卡片布局切换sqlite和mysqlui
     */
    MysqlCard mysqlCard;
    SqliteCard sqliteCard;
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

    public MysqlCard getMysqlCard() {
        return mysqlCard;
    }

    public void setMysqlCard(MysqlCard mysqlCard) {
        this.mysqlCard = mysqlCard;
    }

    public SqliteCard getSqliteCard() {
        return sqliteCard;
    }

    public void setSqliteCard(SqliteCard sqliteCard) {
        this.sqliteCard = sqliteCard;
    }
}
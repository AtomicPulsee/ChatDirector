package com.blalp.chatdirector.modules.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.blalp.chatdirector.configuration.Configuration;
import com.blalp.chatdirector.model.ILoadable;

public class SQLConnection implements ILoadable {
    String connectionString;
    public Connection connection;

    public SQLConnection(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public void load() {
        if(Configuration.debug){
            System.out.println("Loading "+this);
        }
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.err.println("connection "+connectionString+" failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void unload() {
        if(Configuration.debug){
            System.out.println("Unloading "+this);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to unload "+connectionString);
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        unload();
        load();
    }
}

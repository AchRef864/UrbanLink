package org.example.urbanlink.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "jdbc:mysql://localhost:3306/urbanlink";

    public static MyDatabase instance;

    private Connection connection;

    private MyDatabase() {
        try{
            connection = DriverManager.getConnection(DATABASE,USERNAME,PASSWORD);
            System.out.println("Connected to database");
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance(){
        if(instance == null) instance = new MyDatabase();
        return instance;
    }

    public Connection getConnection(){return connection;}
}

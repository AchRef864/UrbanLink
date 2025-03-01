package tn.esprit.jdbc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String USER = "root";
    private final String PWD = "";
    private final String URL = "jdbc:mysql://localhost:3306/urbanlink";

    //1st STEP
    public static MyDatabase instance;

    private Connection cnx;

    //2ND STEP
    private MyDatabase(){
        try {
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection Etablie !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    //3RD STEP
    public static MyDatabase getInstance(){
        if (instance == null) instance = new MyDatabase();
        return instance;
    }

    public Connection getCnx(){
        return cnx;
    }
}

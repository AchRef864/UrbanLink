package utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private final String USER = "root";
    private final String PWD = "";
    private final String URL = "jdbc:mysql://localhost:3306/merpi";

    //1st STEP
    public static MyDataBase instance;

    private Connection connection;

    //2ND STEP
    private MyDataBase(){
        try {
            connection = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connection Etablie !");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    //3RD STEP
    public static MyDataBase getInstance(){
        if (instance == null) instance = new MyDataBase();
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }


}

package tn.esprit.jdbc.tests;
import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.utils.MyDatabase;
import tn.esprit.jdbc.entities.User ;

import java.sql.SQLException;

public class MainTest {

    public static void main(String[] args) {
        MyDatabase m1 = MyDatabase.getInstance();

        User s1 = new User("Hbib","Kham",88);

         UserService userService = new UserService() ;
        try {
            userService.insert(s1);

            System.out.println(userService.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}

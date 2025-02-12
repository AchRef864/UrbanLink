package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.services.UserService;
import tn.esprit.jdbc.utils.MyDatabase;
import tn.esprit.jdbc.entities.User;

import java.sql.SQLException;

public class MainTest {

    public static void main(String[] args) {
        // Get the database instance
        MyDatabase m1 = MyDatabase.getInstance();

        // Create a UserService object to interact with the database
        UserService userService = new UserService();

        try {
            // Step 1: Show all users (initially empty)
            System.out.println("Initial Users:");
            System.out.println(userService.showAll());

            // Step 2: Add 2 users
            User s1 = new User("Hbib", "Kham", 88);
            User s2 = new User("Alice", "Smith", 25);

            userService.insert(s1);
            userService.insert(s2);

            // Step 3: Print the updated data after adding users
            System.out.println(" /n Users after adding 2 users:");
            System.out.println(userService.showAll());

            // Step 4: Delete one user and update the other
            // Delete the user with firstname "Hbib"
            userService.delete(s1);

            // Update the user with firstname "Alice" to change lastname to "Johnson" and age to 30
            User updatedUser = new User("Alice", "Johnson", 30);
            userService.update(updatedUser);

            // Step 5: Print the data for the third time
            System.out.println("\nUsers after deleting one user and updating the other:");
            System.out.println(userService.showAll());

        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
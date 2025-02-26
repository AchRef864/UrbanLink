package tn.esprit.jdbc.entities;

public class Admin extends User {
    // Constructor
    public Admin() {
        super();
        this.setRole("admin"); // Set the role to "admin"
    }

    public Admin(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, "admin"); // Set the role to "admin"
    }
}
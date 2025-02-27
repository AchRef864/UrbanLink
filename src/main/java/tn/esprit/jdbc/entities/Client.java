package tn.esprit.jdbc.entities;

public class Client extends User {
    // Constructor
    public Client() {
        super();
        this.setRole("client"); // Set the role to "client"
    }

    public Client(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, "client"); // Set the role to "client"
    }
}
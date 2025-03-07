package tn.esprit.jdbc.entities;

public class User {
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int admin; // 1 for admin, 0 for client

    // Constructors, getters, and setters
    public User() {}

    public User(String name, String email, String phone, String password, int admin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.admin = admin;
    }

    public User(int userId, String name, String email, String phone, String password, int admin) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.admin = admin;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                '}';
    }
}
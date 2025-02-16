package tn.esprit.models;

public class User {
    private int user_id; // Nommage avec underscore
    private String name;
    private String email;
    private String phone;
    private String password;

    // Constructeur par défaut
    public User() {}

    // Constructeur avec paramètres
    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User(int userId, String name, String email, String phone, String password) {
        this.user_id = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // Getters & Setters (avec underscore)
    public int getuser_id() {
        return user_id;
    }

    public void setuser_id(int user_id) {
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
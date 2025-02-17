package tn.esprit.services;

import tn.esprit.models.User;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements CrudService<User> {

    private final Connection connection;

    public ServiceUser() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String query = "INSERT INTO users (name, email, phone, password, admin) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());

            // Gestion du numéro nullable
            if(user.getPhone() == null || user.getPhone().isEmpty()) {
                pstmt.setNull(3, Types.VARCHAR);
            } else {
                pstmt.setString(3, user.getPhone());
            }

            pstmt.setString(4, user.getPassword());
            pstmt.setInt(5, user.getAdmin());

            pstmt.executeUpdate();

            // Récupération de l'ID auto-généré
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setuser_id(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(User user) throws SQLException {
        String query = "UPDATE users SET name=?, email=?, phone=?, password=?, admin=? WHERE user_id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());

            if(user.getPhone() == null || user.getPhone().isEmpty()) {
                pstmt.setNull(3, Types.VARCHAR);
            } else {
                pstmt.setString(3, user.getPhone());
            }

            pstmt.setString(4, user.getPassword());
            pstmt.setInt(5, user.getAdmin());
            pstmt.setInt(6, user.getuser_id());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM users WHERE user_id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<User> afficher() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getInt("admin")
                );
                users.add(user);
            }
        }
        return users;
    }

    public User trouverParEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getInt("admin")
                    );
                }
            }
        }
        return null;
    }

    // Méthode supplémentaire pour vérifier les identifiants
    public User authentifier(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getInt("admin")
                    );
                }
            }
        }
        return null;
    }
}
package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements CRUD<User> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private PreparedStatement ps;

    @Override
    public int insert(User user) throws SQLException {
        String req = "INSERT INTO `users`(`name`, `email`, `phone`, `password`, `admin`) VALUES (?, ?, ?, ?, ?)";

        ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPhone());
        ps.setString(4, user.getPassword());
        ps.setInt(5, user.getAdmin()); // Add the admin field

        int rowsAffected = ps.executeUpdate();

        // Retrieve the auto-generated user_id
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setUserId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return rowsAffected;
    }

    @Override
    public int update(User user) throws SQLException {
        // Start building the SQL query dynamically based on non-null fields
        StringBuilder queryBuilder = new StringBuilder("UPDATE `users` SET ");
        List<Object> parameters = new ArrayList<>();

        if (user.getName() != null) {
            queryBuilder.append("`name` = ?, ");
            parameters.add(user.getName());
        }
        if (user.getEmail() != null) {
            queryBuilder.append("`email` = ?, ");
            parameters.add(user.getEmail());
        }
        if (user.getPhone() != null) {
            queryBuilder.append("`phone` = ?, ");
            parameters.add(user.getPhone());
        }
        if (user.getPassword() != null) {
            queryBuilder.append("`password` = ?, ");
            parameters.add(user.getPassword());
        }

        // Remove the trailing comma and space
        if (parameters.isEmpty()) {
            throw new SQLException("No fields provided for update.");
        }
        queryBuilder.setLength(queryBuilder.length() - 2); // Remove the last ", "

        // Add the WHERE clause
        queryBuilder.append(" WHERE `user_id` = ?");
        parameters.add(user.getUserId());

        // Prepare and execute the statement
        ps = cnx.prepareStatement(queryBuilder.toString());
        for (int i = 0; i < parameters.size(); i++) {
            ps.setObject(i + 1, parameters.get(i));
        }

        return ps.executeUpdate();
    }

    @Override
    public int delete(int userId) throws SQLException {
        String req = "DELETE FROM `users` WHERE `user_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, userId);
        return ps.executeUpdate();
    }

    @Override
    public List<User> showAll() throws SQLException {
        List<User> temp = new ArrayList<>();

        String req = "SELECT * FROM `users`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setPassword(rs.getString("password"));
            user.setAdmin(rs.getInt("admin"));

            temp.add(user);
        }

        return temp;
    }

    public User authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

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
        return null; // User not found
    }

    public boolean isEmailInUse(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void addUser(User user) throws SQLException {
        insert(user);
    }
}
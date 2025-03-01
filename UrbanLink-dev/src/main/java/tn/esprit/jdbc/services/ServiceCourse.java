package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Course;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceCourse implements CRUD<Course> {
    private final Connection connection;

    public ServiceCourse() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    @Override
    public int insert(Course course) throws SQLException {
        String query = "INSERT INTO course (user_id, id_taxi, date_course, ville_depart, ville_arrivee, distance_km, montant, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, course.getUser_id());
            pstmt.setInt(2, course.getId_taxi());
            pstmt.setTimestamp(3, Timestamp.valueOf(course.getDate_course()));
            pstmt.setString(4, course.getVille_depart());
            pstmt.setString(5, course.getVille_arrivee());
            pstmt.setDouble(6, course.getDistance_km());
            pstmt.setDouble(7, course.getMontant());
            pstmt.setString(8, course.getStatut());
            int rowsAffected = pstmt.executeUpdate();
            int generatedId = -1;
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    course.setId_course(generatedId);
                }
            }
            return generatedId;
        }
    }

    @Override
    public int update(Course course) throws SQLException {
        String query = "UPDATE course SET user_id=?, id_taxi=?, date_course=?, ville_depart=?, ville_arrivee=?, distance_km=?, montant=?, statut=? WHERE id_course=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, course.getUser_id());
            pstmt.setInt(2, course.getId_taxi());
            pstmt.setTimestamp(3, Timestamp.valueOf(course.getDate_course()));
            pstmt.setString(4, course.getVille_depart());
            pstmt.setString(5, course.getVille_arrivee());
            pstmt.setDouble(6, course.getDistance_km());
            pstmt.setDouble(7, course.getMontant());
            pstmt.setString(8, course.getStatut());
            pstmt.setInt(9, course.getId_course());
            return pstmt.executeUpdate();
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        String query = "DELETE FROM course WHERE id_course=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    @Override
    public List<Course> showAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM course";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id_course"),
                        rs.getInt("user_id"),
                        rs.getInt("id_taxi"),
                        rs.getTimestamp("date_reservation") != null
                                ? rs.getTimestamp("date_reservation").toLocalDateTime()
                                : LocalDateTime.now(),
                        rs.getTimestamp("date_course").toLocalDateTime(),
                        rs.getString("ville_depart"),
                        rs.getString("ville_arrivee"),
                        rs.getDouble("distance_km"),
                        rs.getDouble("montant"),
                        rs.getString("statut")
                );
                courses.add(course);
            }
        }
        return courses;
    }


}

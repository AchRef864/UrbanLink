package tn.esprit.services;

import tn.esprit.models.Course;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceCourse implements CrudService<Course> {

    private final Connection connection;

    public ServiceCourse() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Course course) throws SQLException {
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

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId_course(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(Course course) throws SQLException {
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

            pstmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM course WHERE id_course=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Course> afficher() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM course";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id_course"),
                        rs.getInt("user_id"),
                        rs.getInt("id_taxi"),
                        rs.getTimestamp("date_reservation").toLocalDateTime(),
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
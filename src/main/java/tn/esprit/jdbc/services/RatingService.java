package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Rating;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.jdbc.entities.Rating;

public class RatingService {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    public void addRating(Rating rating) throws SQLException {
        String query = "INSERT INTO ratings (user_id, vehicle_id, taxi_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, rating.getUserId());
            ps.setInt(2, rating.getVehicleId()); // Set vehicle_id (can be 0 if not applicable)
            ps.setInt(3, rating.getTaxiId());   // Set taxi_id (can be 0 if not applicable)
            ps.setInt(4, rating.getRating());
            ps.setString(5, rating.getComment());
            ps.executeUpdate();
        }
    }

    // Get all ratings for a specific vehicle
    public List<Rating> getRatingsByVehicle(int vehicleId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String query = "SELECT * FROM ratings WHERE vehicle_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("id"));
                rating.setUserId(rs.getInt("user_id"));
                rating.setVehicleId(rs.getInt("vehicle_id"));
                rating.setRating(rs.getInt("rating"));
                rating.setComment(rs.getString("comment"));
                rating.setTimestamp(rs.getTimestamp("timestamp"));
                ratings.add(rating);
            }
        }
        return ratings;
    }

    // Get the average rating for a specific vehicle
    public double getAverageRatingForVehicle(int vehicleId) throws SQLException {
        String query = "SELECT AVG(rating) AS average FROM ratings WHERE vehicle_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average");
            }
        }
        return 0.0;
    }

    // Delete a rating
    public void deleteRating(int ratingId) throws SQLException {
        String query = "DELETE FROM ratings WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, ratingId);
            ps.executeUpdate();
        }
    }

    // Get all ratings
    public List<Rating> getAllRatings() throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String query = "SELECT * FROM ratings";
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rating rating = new Rating();
                rating.setId(rs.getInt("id"));
                rating.setUserId(rs.getInt("user_id"));
                rating.setVehicleId(rs.getInt("vehicle_id"));
                rating.setRating(rs.getInt("rating"));
                rating.setComment(rs.getString("comment"));
                rating.setTimestamp(rs.getTimestamp("timestamp"));
                ratings.add(rating);
            }
        }
        return ratings;
    }

    // Get the average rating across all ratings
    public double getAverageRating() throws SQLException {
        String query = "SELECT AVG(rating) AS average_rating FROM ratings";
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("average_rating");
            }
        }
        return 0.0;
    }
}
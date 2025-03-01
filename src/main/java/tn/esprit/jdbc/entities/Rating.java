package tn.esprit.jdbc.entities;

import java.sql.Timestamp;

public class Rating {
    private int id;
    private int userId;
    private int vehicleId; // For vehicle ratings
    private int taxiId;    // For taxi ratings
    private int rating;
    private String comment;
    private Timestamp timestamp;

    // Constructors
    public Rating() {}

    public Rating(int userId, int vehicleId, int taxiId, int rating, String comment) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.taxiId = taxiId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", userId=" + userId +
                ", vehicleId=" + vehicleId +
                ", taxiId=" + taxiId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
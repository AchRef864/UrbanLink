package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.entities.VehicleType;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tn.esprit.jdbc.entities.Vehicle;

public class vehicleService {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    public int insert(Vehicle vehicle) throws SQLException {
        String query = "INSERT INTO vehicule (model, license_plate, type, capacity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vehicle.getModel());
            ps.setString(2, vehicle.getLicensePlate());
            ps.setString(3, vehicle.getType().name()); // Convert ENUM to String
            ps.setInt(4, vehicle.getCapacity());
            ps.executeUpdate();

            // Retrieve generated vehicle ID
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return -1; // Return -1 if no ID was generated
    }

    public int update(Vehicle vehicle) throws SQLException {
        String query = "UPDATE vehicule SET model = ?, license_plate = ?, type = ?, capacity = ? WHERE vehicle_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, vehicle.getModel());
            ps.setString(2, vehicle.getLicensePlate());
            ps.setString(3, vehicle.getType().name()); // Convert ENUM to String
            ps.setInt(4, vehicle.getCapacity());
            ps.setInt(5, vehicle.getVehicleId());
            return ps.executeUpdate();
        }
    }

    public int delete(int vehicleId) throws SQLException {
        String query = "DELETE FROM vehicule WHERE vehicle_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            return ps.executeUpdate();
        }
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicule";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        VehicleType.valueOf(rs.getString("type")), // Convert String to ENUM
                        rs.getInt("capacity")
                );
                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    public int countVehicles() throws SQLException {
        String query = "SELECT COUNT(*) AS vehicle_count FROM vehicule";
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("vehicle_count");
            }
        }
        return 0;
    }

    public Vehicle getById(int vehicleId) throws SQLException {
        String query = "SELECT * FROM vehicule WHERE vehicle_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        VehicleType.valueOf(rs.getString("type")), // Convert String to ENUM
                        rs.getInt("capacity")
                );
                vehicle.setVehicleId(vehicleId);
                return vehicle;
            }
        }
        return null;
    }

    public List<String> getAllVehicleLicensePlates() throws SQLException {
        List<String> licensePlates = new ArrayList<>();
        String query = "SELECT license_plate FROM vehicule";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                licensePlates.add(rs.getString("license_plate"));
            }
        }
        return licensePlates;
    }

    public int getVehicleIdByLicensePlate(String licensePlate) throws SQLException {
        String query = "SELECT vehicle_id FROM vehicule WHERE license_plate = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, licensePlate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("vehicle_id");
            }
        }
        return -1; // Return -1 if no matching vehicle is found
    }
    //showAll
    public List<Vehicle> showAll() throws SQLException {
        List<Vehicle> temp = new ArrayList<>();

        String req = "SELECT * FROM `vehicule`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleId(rs.getInt("vehicle_id"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setLicensePlate(rs.getString("license_plate"));
            vehicle.setType(VehicleType.valueOf(rs.getString("type")));
            vehicle.setCapacity(rs.getInt("capacity"));

            temp.add(vehicle);
        }

        return temp;
    }
}
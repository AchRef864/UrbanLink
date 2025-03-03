package tn.esprit.jdbc.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.entities.VehicleType;
import tn.esprit.jdbc.utils.MyDatabase;

public class vehicleService {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    private static final String CAR_REGEX = "\\d{3}-\\d{4}"; // Example: 123-4567
    private static final String BUS_REGEX = "\\d{2}-\\d{3}-\\d{2}"; // Example: 12-345-67
    private static final String TRUCK_REGEX = "\\d{4}-[A-Z]{2}"; // Example: 1234-AB
    private static final String MOTORCYCLE_REGEX = "[A-Z]{2}-\\d{3}-[A-Z]"; // Example: AB-123-C

    public int insert(Vehicle vehicle) throws SQLException {
        if (!isValidLicensePlate(vehicle.getLicensePlate(), vehicle.getType())) {
            throw new IllegalArgumentException("Invalid license plate format for " + vehicle.getType());
        }
        if (!isValidCapacity(vehicle.getCapacity(), vehicle.getType())) {
            throw new IllegalArgumentException("Invalid capacity for " + vehicle.getType());
        }

        String query = "INSERT INTO vehicule (model, license_plate, type, capacity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vehicle.getModel());
            ps.setString(2, vehicle.getLicensePlate());
            ps.setString(3, vehicle.getType().name()); // Convert ENUM to String
            ps.setInt(4, vehicle.getCapacity());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    public int update(Vehicle vehicle) throws SQLException {
        if (!isValidLicensePlate(vehicle.getLicensePlate(), vehicle.getType())) {
            throw new IllegalArgumentException("Invalid license plate format for " + vehicle.getType());
        }
        if (!isValidCapacity(vehicle.getCapacity(), vehicle.getType())) {
            throw new IllegalArgumentException("Invalid capacity for " + vehicle.getType());
        }

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
        String query = "SELECT v.*, u.user_id, u.name, u.email, u.phone, u.password, u.role " +
                "FROM vehicule v " +
                "JOIN user u ON v.user_id = u.user_id";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                // Creating the driver (User) object
                User driver = new User();
                driver.setUserId(rs.getInt("user_id"));
                driver.setName(rs.getString("name"));
                driver.setEmail(rs.getString("email"));
                driver.setPhone(rs.getString("phone"));
                driver.setPassword(rs.getString("password")); // Be careful with security
                driver.setRole(rs.getString("role"));

                // Creating the vehicle object
                Vehicle vehicle = new Vehicle(
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        VehicleType.valueOf(rs.getString("type")), // Convert String to ENUM
                        rs.getInt("capacity")
                );
                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setDriver(driver); // Set the driver

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
        return -1;
    }

    public List<Vehicle> showAll() throws SQLException {
        List<Vehicle> temp = new ArrayList<>();
        String req = "SELECT * FROM vehicule";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setType(VehicleType.valueOf(rs.getString("type")));
                vehicle.setCapacity(rs.getInt("capacity"));

                temp.add(vehicle);
            }
        }
        return temp;
    }

    private boolean isValidLicensePlate(String licensePlate, VehicleType type) {
        String pattern;
        switch (type) {
            case Car:
                pattern = CAR_REGEX;
                break;
            case Bus:
                pattern = BUS_REGEX;
                break;
            case Truck:
                pattern = TRUCK_REGEX;
                break;
            case Motorcycle:
                pattern = MOTORCYCLE_REGEX;
                break;
            default:
                return false;
        }
        return Pattern.matches(pattern, licensePlate);
    }

    private boolean isValidCapacity(int capacity, VehicleType type) {
        switch (type) {
            case Car:
                return capacity >= 1 && capacity <= 7;
            case Bus:
                return capacity >= 10 && capacity <= 50;
            case Truck:
                return capacity >= 2 && capacity <= 10;
            case Motorcycle:
                return capacity == 1 || capacity == 2;
            default:
                return false;
        }
    }
}

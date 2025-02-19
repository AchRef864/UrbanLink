package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.entities.VehicleType;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class vehicleService implements CRUD<Vehicle> {
    private final Connection connection;

    public vehicleService() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    @Override
    public int insert(Vehicle vehicle) throws SQLException {
        String query = "INSERT INTO `vehicule` (`model`, `license_plate`, `type`, `capacity`) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, vehicle.getModel());
        statement.setString(2, vehicle.getLicensePlate());
        statement.setString(3, vehicle.getType().name());  // Convert ENUM to String
        statement.setInt(4, vehicle.getCapacity());

        int affectedRows = statement.executeUpdate();

        // Retrieve generated vehicle ID
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            vehicle.setVehicleId(generatedKeys.getInt(1));
        }

        return affectedRows;
    }

    @Override
    public int update(Vehicle vehicle) throws SQLException {
        String query = "UPDATE `vehicule` SET `model` = ?, `license_plate` = ?, `type` = ?, `capacity` = ? WHERE `vehicle_id` = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, vehicle.getModel());
        statement.setString(2, vehicle.getLicensePlate());
        statement.setString(3, vehicle.getType().name()); // Store enum as String
        statement.setInt(4, vehicle.getCapacity());
        statement.setInt(5, vehicle.getVehicleId());

        return statement.executeUpdate();
    }

    @Override
    public int delete(int vehicleId) throws SQLException {
        String query = "DELETE FROM vehicule WHERE vehicle_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, vehicleId);

        return statement.executeUpdate();
    }

    @Override
    public List<Vehicle> showAll() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicule";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            Vehicle vehicle = new Vehicle(
                    resultSet.getString("model"),
                    resultSet.getString("license_plate"),
                    VehicleType.valueOf(resultSet.getString("type")),  // Convert String to Enum
                    resultSet.getInt("capacity")
            );
            vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
            vehicles.add(vehicle);
        }
        return vehicles;
    }

    public Vehicle getById(int vehicleId) throws SQLException {
        String query = "SELECT * FROM vehicule WHERE vehicle_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, vehicleId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Vehicle vehicle = new Vehicle(
                    resultSet.getString("model"),
                    resultSet.getString("license_plate"),
                    VehicleType.valueOf(resultSet.getString("type")),  // Convert String to ENUM
                    resultSet.getInt("capacity")
            );
            vehicle.setVehicleId(vehicleId);
            return vehicle;
        }
        return null;
    }

    public Vehicle getByLicensePlate(String licensePlate) throws SQLException {
        String query = "SELECT * FROM vehicule WHERE license_plate = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, licensePlate);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Vehicle vehicle = new Vehicle(
                    resultSet.getString("model"),
                    resultSet.getString("license_plate"),
                    VehicleType.valueOf(resultSet.getString("type")),  // Convert String to ENUM
                    resultSet.getInt("capacity")
            );
            vehicle.setVehicleId(resultSet.getInt("vehicle_id")); // Retrieve the auto-incremented ID
            return vehicle;
        }
        return null; // No vehicle found with this license plate
    }
}

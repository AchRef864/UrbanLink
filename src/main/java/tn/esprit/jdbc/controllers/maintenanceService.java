package tn.esprit.jdbc.controllers;

import tn.esprit.jdbc.entities.Maintenance;
import tn.esprit.jdbc.entities.Vehicle;
import tn.esprit.jdbc.services.CRUD;
import tn.esprit.jdbc.services.vehicleService;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class maintenanceService implements CRUD<Maintenance> {
    private final Connection connection;

    public maintenanceService() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    @Override
    public int insert(Maintenance maintenance) throws SQLException {
        String query = "INSERT INTO maintenance (vehicle_id, description, maintenance_date, cost) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, maintenance.getVehicle().getVehicleId());
        statement.setString(2, maintenance.getDescription());
        statement.setDate(3, new Date(maintenance.getMaintenanceDate().getTime()));
        statement.setDouble(4, maintenance.getCost());

        return statement.executeUpdate();
    }

    @Override
    public int update(Maintenance maintenance) throws SQLException {
        String query = "UPDATE maintenance SET vehicle_id = ?, description = ?, maintenance_date = ?, cost = ? WHERE maintenance_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, maintenance.getVehicle().getVehicleId());
        statement.setString(2, maintenance.getDescription());
        statement.setDate(3, new Date(maintenance.getMaintenanceDate().getTime()));
        statement.setDouble(4, maintenance.getCost());
        statement.setInt(5, maintenance.getMaintenanceId());

        return statement.executeUpdate();
    }

    @Override
    public int delete(int maintenanceId) throws SQLException {
        String query = "DELETE FROM maintenance WHERE maintenance_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, maintenanceId);

        return statement.executeUpdate();
    }

    @Override
    public List<Maintenance> showAll() throws SQLException {
        List<Maintenance> maintenanceRecords = new ArrayList<>();
        String query = "SELECT * FROM maintenance";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        vehicleService vehicleService = new vehicleService(); // Create an instance

        while (resultSet.next()) {
            int vehicleId = resultSet.getInt("vehicle_id");
            Vehicle vehicle = vehicleService.getById(vehicleId);

            Maintenance maintenance = new Maintenance(
                    vehicle,
                    resultSet.getString("description"),
                    resultSet.getDate("maintenance_date"),
                    resultSet.getDouble("cost")
            );

            // Set the maintenance_id correctly
            maintenance.setMaintenanceId(resultSet.getInt("maintenance_id"));
            System.out.println(maintenance.getMaintenanceId());
            maintenanceRecords.add(maintenance);
        }
        return maintenanceRecords;
    }

    public List<Maintenance> getMaintenanceByVehicleId(int vehicleId) throws SQLException {
        List<Maintenance> maintenanceRecords = new ArrayList<>();

        String query = "SELECT * FROM maintenance WHERE vehicle_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, vehicleId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Maintenance maintenance = new Maintenance(
                    null, // Vehicle is not needed here
                    resultSet.getString("description"),
                    resultSet.getDate("maintenance_date"),
                    resultSet.getDouble("cost")
            );
            maintenance.setMaintenanceId(resultSet.getInt("maintenance_id"));
            maintenanceRecords.add(maintenance);
        }

        return maintenanceRecords;
    }
}

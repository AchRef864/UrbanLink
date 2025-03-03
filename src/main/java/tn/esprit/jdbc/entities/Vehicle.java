package tn.esprit.jdbc.entities;

import java.util.List;

public class Vehicle
{
    private int vehicleId;
    private String model;
    private String licensePlate;
    private VehicleType type;  // Use enum instead of String
    private int capacity;
    private User driver;
    private List<Maintenance> maintenanceRecords;

    // Constructor
    public Vehicle(String model, String licensePlate, VehicleType type, int capacity) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.type = type;
        this.capacity = capacity;
    }
    //add a constructor without parameters in the Vehicle class
    public Vehicle() {
    }
    // Getters & Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<Maintenance> getMaintenanceRecords() { return maintenanceRecords; }
    public void setMaintenanceRecords(List<Maintenance> maintenanceRecords) { this.maintenanceRecords = maintenanceRecords; }

    public User getDriver() { return driver; }
    public void setDriver(User driver) { this.driver = driver; }

    @Override
    public String toString() {
        return model + " - " + licensePlate; // Adjust according to your field names
    }

}
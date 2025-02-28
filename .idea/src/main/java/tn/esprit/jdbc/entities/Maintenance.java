package tn.esprit.jdbc.entities;

import java.util.Date;

public class Maintenance {
    private int maintenanceId;
    private Vehicle vehicle;
    private String description;
    private Date maintenanceDate;
    private double cost;

    // Constructor
    public Maintenance(Vehicle vehicle, String description, Date maintenanceDate, double cost) {
        this.vehicle = vehicle;
        this.description = description;
        this.maintenanceDate = maintenanceDate;
        this.cost = cost;
    }

    // Getters & Setters
    public int getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(int maintenanceId) { this.maintenanceId = maintenanceId; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(Date maintenanceDate) { this.maintenanceDate = maintenanceDate; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
}


package tn.esprit.jdbc.entities;

import java.util.*;

public class Trajet {

    private int id;
    private String departure;
    private String destination;
    private double distance;
    private String duration;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private List<Vehicle> vehiclelist;

    // Default constructor
    public Trajet() {
        this.vehiclelist = new ArrayList<>();  // Initialize vehicle list
    }

    // Constructor without id
    public Trajet(String departure, String destination, double distance, String duration, String departureTime, String arrivalTime, double price) {
        this.departure = departure;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.vehiclelist = new ArrayList<>();  // Initialize vehicle list
    }

    // Constructor with id
    public Trajet(int id, String departure, String destination, double distance, String duration, String departureTime, String arrivalTime, double price) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.vehiclelist = new ArrayList<>();  // Initialize vehicle list
    }

    // Constructor with id only
    public Trajet(int id) {
        this.id = id;
        this.vehiclelist = new ArrayList<>();  // Initialize vehicle list
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Vehicle> getVehiclelist() {
        return vehiclelist;
    }

    public void setVehiclelist(List<Vehicle> vehiclelist) {
        this.vehiclelist = vehiclelist;
    }

    // toString method using StringBuilder for efficiency
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Trajet{");
        sb.append("id=").append(id);
        sb.append(", departure='").append(departure).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", distance=").append(distance);
        sb.append(", duration='").append(duration).append('\'');
        sb.append(", departureTime='").append(departureTime).append('\'');
        sb.append(", arrivalTime='").append(arrivalTime).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

    // equals method with null checks using Objects.equals
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Trajet trajet = (Trajet) object;
        return id == trajet.id &&
                Double.compare(trajet.distance, distance) == 0 &&
                Double.compare(trajet.price, price) == 0 &&
                Objects.equals(departure, trajet.departure) &&
                Objects.equals(destination, trajet.destination) &&
                Objects.equals(duration, trajet.duration) &&
                Objects.equals(departureTime, trajet.departureTime) &&
                Objects.equals(arrivalTime, trajet.arrivalTime);
    }

    // hashCode method using Objects.hash
    @Override
    public int hashCode() {
        return Objects.hash(id, departure, destination, distance, duration, departureTime, arrivalTime, price);
    }
}

package tn.esprit.jdbc.entities;

import java.util.*;

public class Trajet {

    private int id;
    private String departure;
    private String destination;
    private double distance;
    private String duration; // duration as double (e.g., in hours or minutes)
    private String departureTime;
    private String arrivalTime;
    private double price;
    private TrajetType trajetType; // New field to store the type of the trajet
    private List<Vehicle> vehicleList;

    // Default constructor
    public Trajet() {
        this.vehicleList = new ArrayList<>();  // Initialize vehicle list
    }

    // Constructor without id
    public Trajet(String departure, String destination, double distance, String duration, String departureTime, String arrivalTime, double price, TrajetType trajetType) {
        this.departure = departure;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.trajetType = trajetType;  // Initialize trajetType
        this.vehicleList = new ArrayList<>();  // Initialize vehicle list
    }

    // Constructor with id
    public Trajet(int id, String departure, String destination, double distance, String duration, String departureTime, String arrivalTime, double price, TrajetType trajetType) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.trajetType = trajetType;  // Initialize trajetType
        this.vehicleList = new ArrayList<>();  // Initialize vehicle list
    }

    // Constructor with id only
    public Trajet(int id) {
        this.id = id;
        this.vehicleList = new ArrayList<>();  // Initialize vehicle list
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

    public TrajetType getTrajetType() {
        return trajetType;
    }

    public void setTrajetType(TrajetType trajetType) {
        this.trajetType = trajetType;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    // toString method using StringBuilder for efficiency
    @Override
    public String toString() {
        return "Trajet{" +
                "departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                ", price=" + price +
                ", trajetType=" + trajetType +  // Include trajetType in toString
                '}';
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
                Objects.equals(departureTime, trajet.departureTime) &&
                Objects.equals(arrivalTime, trajet.arrivalTime) &&
                trajetType == trajet.trajetType;  // Include trajetType in equals
    }

    // hashCode method using Objects.hash
    @Override
    public int hashCode() {
        return Objects.hash(id, departure, destination, distance, duration, departureTime, arrivalTime, price, trajetType);
    }
}

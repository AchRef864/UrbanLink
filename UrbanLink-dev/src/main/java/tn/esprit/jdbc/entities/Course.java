package tn.esprit.jdbc.entities;

import java.time.LocalDateTime;

public class Course {
    private int id_course;
    private int user_id;
    private int id_taxi;
    private LocalDateTime date_reservation;
    private LocalDateTime date_course;
    private String ville_depart;
    private String ville_arrivee;
    private double distance_km;
    private double montant;
    private String statut;

    // Constructeurs
    public Course() {}


    public Course(int user_id, int id_taxi, LocalDateTime date_course,
                  String ville_depart, String ville_arrivee, double distance_km,
                  double montant, String statut) {
        this.user_id = user_id;
        this.id_taxi = id_taxi;
        this.date_course = date_course;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.distance_km = distance_km;
        this.montant = montant;
        this.statut = statut;
    }

    public Course(int idCourse, int userId, int idTaxi, LocalDateTime dateReservation, LocalDateTime dateCourse, String villeDepart, String villeArrivee, double distanceKm, double montant, String statut) {
        this.id_course = idCourse;
        this.user_id = userId;
        this.id_taxi = idTaxi;
        this.date_reservation = dateReservation;
        this.date_course = dateCourse;
        this.ville_depart = villeDepart;
        this.ville_arrivee = villeArrivee;
        this.distance_km = distanceKm;
        this.montant = montant;
        this.statut = statut;
    }

    // Getters & Setters (avec underscores)
    public int getId_course() {
        return id_course;
    }

    public void setId_course(int id_course) {
        this.id_course = id_course;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId_taxi() {
        return id_taxi;
    }

    public void setId_taxi(int id_taxi) {
        this.id_taxi = id_taxi;
    }

    public LocalDateTime getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(LocalDateTime date_reservation) {
        this.date_reservation = date_reservation;
    }

    public LocalDateTime getDate_course() {
        return date_course;
    }

    public void setDate_course(LocalDateTime date_course) {
        this.date_course = date_course;
    }

    public String getVille_depart() {
        return ville_depart;
    }

    public void setVille_depart(String ville_depart) {
        this.ville_depart = ville_depart;
    }

    public String getVille_arrivee() {
        return ville_arrivee;
    }

    public void setVille_arrivee(String ville_arrivee) {
        this.ville_arrivee = ville_arrivee;
    }

    public double getDistance_km() {
        return distance_km;
    }

    public void setDistance_km(double distance_km) {
        this.distance_km = distance_km;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id_course=" + id_course +
                ", user_id=" + user_id +
                ", id_taxi=" + id_taxi +
                ", date_reservation=" + date_reservation +
                ", date_course=" + date_course +
                ", ville_depart='" + ville_depart + '\'' +
                ", ville_arrivee='" + ville_arrivee + '\'' +
                ", distance_km=" + distance_km +
                ", montant=" + montant +
                ", statut='" + statut + '\'' +
                '}';
    }
}
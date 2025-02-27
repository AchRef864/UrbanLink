package tn.esprit.jdbc.entities;

import java.sql.Date;

public class Taxii {
    private int idTaxi;
    private String immatriculation;
    private String marque;
    private String modele;
    private int anneeFabrication;
    private int capacite;
    private String zoneDesserte;
    private String licenceNumero;
    private Date licenceDateObtention;
    private double tarifBase;
    private int userId;
    private int maintenanceId;

    // Constructor, getters, and setters
    public Taxii(int idTaxi, String immatriculation, String marque, String modele, int anneeFabrication, int capacite, String zoneDesserte, String licenceNumero, Date licenceDateObtention, double tarifBase, int userId, int maintenanceId) {
        this.idTaxi = idTaxi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.anneeFabrication = anneeFabrication;
        this.capacite = capacite;
        this.zoneDesserte = zoneDesserte;
        this.licenceNumero = licenceNumero;
        this.licenceDateObtention = licenceDateObtention;
        this.tarifBase = tarifBase;
        this.userId = userId;
        this.maintenanceId = maintenanceId;
    }
    //constructor have all parameters
    public Taxii(int idTaxi, String immatriculation, String marque, String modele, int anneeFabrication, int capacite, String zoneDesserte, String licenceNumero, Date licenceDateObtention, double tarifBase, int userId) {
        this.idTaxi = idTaxi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.anneeFabrication = anneeFabrication;
        this.capacite = capacite;
        this.zoneDesserte = zoneDesserte;
        this.licenceNumero = licenceNumero;
        this.licenceDateObtention = licenceDateObtention;
        this.tarifBase = tarifBase;
        this.userId = userId;
    }

    // Getters and setters for all fields
    public int getIdTaxi() {
        return idTaxi;
    }

    public void setIdTaxi(int idTaxi) {
        this.idTaxi = idTaxi;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }
}
package tn.esprit.jdbc.entities;

<<<<<<< HEAD
import java.time.LocalDate;
=======
import java.sql.Date;

>>>>>>> e244cabab4672bf5258bf2c96b9c84d2fec77a85
public class Taxi {
    private int idTaxi;
    private String immatriculation;
    private String marque;
    private String modele;
    private int anneeFabrication;
    private int capacite;
    private String zoneDesserte;
<<<<<<< HEAD
    private String statut;
    private String licenceNumero;
    private LocalDate licenceDateObtention;
    private Double tarifBase;

    public Taxi() {}

    public Taxi(String immatriculation, String marque, String modele,
                int anneeFabrication, int capacite, String zoneDesserte,
                String statut, String licenceNumero, LocalDate licenceDateObtention,
                Double tarifBase) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.anneeFabrication = anneeFabrication;
        this.capacite = capacite;
        this.zoneDesserte = zoneDesserte;
        this.statut = statut;
        this.licenceNumero = licenceNumero;
        this.licenceDateObtention = licenceDateObtention;
        this.tarifBase = tarifBase;
    }

    public Taxi(int idTaxi, String immatriculation, String marque, String modele, int anneeFabrication, int capacite, String zoneDesserte, String statut, String licenceNumero, LocalDate licenceDateObtention, Double tarifBase) {
=======
    private String licenceNumero;
    private Date licenceDateObtention;
    private double tarifBase;
    private int userId;
    private int maintenanceId;

    // Constructor, getters, and setters
    public Taxi(int idTaxi, String immatriculation, String marque, String modele, int anneeFabrication, int capacite, String zoneDesserte, String licenceNumero, Date licenceDateObtention, double tarifBase, int userId, int maintenanceId) {
>>>>>>> e244cabab4672bf5258bf2c96b9c84d2fec77a85
        this.idTaxi = idTaxi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.anneeFabrication = anneeFabrication;
        this.capacite = capacite;
        this.zoneDesserte = zoneDesserte;
<<<<<<< HEAD
        this.statut = statut;
        this.licenceNumero = licenceNumero;
        this.licenceDateObtention = licenceDateObtention;
        this.tarifBase = tarifBase;
    }

=======
        this.licenceNumero = licenceNumero;
        this.licenceDateObtention = licenceDateObtention;
        this.tarifBase = tarifBase;
        this.userId = userId;
        this.maintenanceId = maintenanceId;
    }
    //constructor have all parameters
    public Taxi(int idTaxi, String immatriculation, String marque, String modele, int anneeFabrication, int capacite, String zoneDesserte, String licenceNumero, Date licenceDateObtention, double tarifBase, int userId) {
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
>>>>>>> e244cabab4672bf5258bf2c96b9c84d2fec77a85
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
<<<<<<< HEAD

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getLicenceNumero() {
        return licenceNumero;
    }

    public void setLicenceNumero(String licenceNumero) {
        this.licenceNumero = licenceNumero;
    }

    public Double getTarifBase() {
        return tarifBase;
    }

    public void setTarifBase(Double tarifBase) {
        this.tarifBase = tarifBase;
    }

    public LocalDate getLicenceDateObtention() {
        return licenceDateObtention;
    }

    public void setLicenceDateObtention(LocalDate licenceDateObtention) {
        this.licenceDateObtention = licenceDateObtention;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getZoneDesserte() {
        return zoneDesserte;
    }

    public void setZoneDesserte(String zoneDesserte) {
        this.zoneDesserte = zoneDesserte;
    }

    public int getAnneeFabrication() {
        return anneeFabrication;
    }

    public void setAnneeFabrication(int anneeFabrication) {
        this.anneeFabrication = anneeFabrication;
    }

    @Override
    public String toString() {
        return "Taxi{" +
                "idTaxi=" + idTaxi +
                ", immatriculation='" + immatriculation + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", anneeFabrication=" + anneeFabrication +
                ", capacite=" + capacite +
                ", zoneDesserte='" + zoneDesserte + '\'' +
                ", statut='" + statut + '\'' +
                ", licenceNumero='" + licenceNumero + '\'' +
                ", licenceDateObtention=" + licenceDateObtention +
                ", tarifBase=" + tarifBase +
                '}';
    }
    // Assurez-vous que tous les getters existent

}

=======
}
>>>>>>> e244cabab4672bf5258bf2c96b9c84d2fec77a85

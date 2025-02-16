package tn.esprit.models;

import java.time.LocalDate;

public class Contrat {
    private int id_contrat;
    private int id_taxi;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private double montant;
    private String statut;

    // Constructeurs
    public Contrat() {}

    public Contrat(int id_taxi, LocalDate date_debut, LocalDate date_fin, double montant, String statut) {
        this.id_taxi = id_taxi;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.montant = montant;
        this.statut = statut;
    }

    public Contrat(int idContrat, int idTaxi, LocalDate dateDebut, LocalDate dateFin, double montant, String statut) {
        this.id_contrat = idContrat;
        this.id_taxi = idTaxi;
        this.date_debut = dateDebut;
        this.date_fin = dateFin;
        this.montant = montant;
        this.statut = statut;
    }

    // Getters & Setters (avec underscores)
    public int getId_contrat() {
        return id_contrat;
    }

    public void setId_contrat(int id_contrat) {
        this.id_contrat = id_contrat;
    }

    public int getId_taxi() {
        return id_taxi;
    }

    public void setId_taxi(int id_taxi) {
        this.id_taxi = id_taxi;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public LocalDate getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
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
        return "Contrat{" +
                "id_contrat=" + id_contrat +
                ", id_taxi=" + id_taxi +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", montant=" + montant +
                ", statut='" + statut + '\'' +
                '}';
    }
}
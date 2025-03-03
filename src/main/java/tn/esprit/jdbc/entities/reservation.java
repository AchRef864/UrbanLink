package tn.esprit.jdbc.entities;

import java.util.Date;

public class reservation {
    private int id;
    private int userId; // Relation avec un utilisateur
    private Date dateDebut;
    private Date dateFin;

    @Override
    public String toString() {
        return "reservation{" +
                "id=" + id +
                ", userId=" + userId +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut='" + statut + '\'' +
                ", abonnement=" + abonnement +
                '}';
    }

    private String statut; // Confirmée, Annulée, En attente
    private abonnement abonnement; // Relation avec un abonnement

    // Constructeurs
    public reservation() {}

    public reservation(int userId, Date dateDebut, Date dateFin, String statut, abonnement abonnement) {
        this.userId = userId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.abonnement = abonnement;
    }

    public reservation(int id, int userId, Date dateDebut, Date dateFin, String statut, abonnement abonnement) {
        this.id = id;
        this.userId = userId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.abonnement = abonnement;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getuserId() { return userId; }
    public void setuserId(int userId) { this.userId = userId; }

    public java.sql.Date getdateDebut() { return (java.sql.Date) dateDebut; }
    public void setdateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public java.sql.Date getdateFin() { return (java.sql.Date) dateFin; }
    public void setdateFin(Date dateFin) { this.dateFin = dateFin; }

    public String getstatut() { return statut; }
    public void setstatut(String statut) { this.statut = statut; }

    public abonnement getabonnement() { return abonnement; }
    public void setabonnement(abonnement abonnement) { this.abonnement = abonnement; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        reservation that = (reservation) o;
        return id == that.id && userId == that.userId && dateDebut.equals(that.dateDebut) &&
                dateFin.equals(that.dateFin) && statut.equals(that.statut) && abonnement.equals(that.abonnement);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + dateDebut.hashCode();
        result = 31 * result + dateFin.hashCode();
        result = 31 * result + statut.hashCode();
        result = 31 * result + abonnement.hashCode();
        return result;
    }
}

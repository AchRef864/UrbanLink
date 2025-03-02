package tn.esprit.jdbc.entities;
import java.util.Date;
import java.util.Objects;
public class abonnement {
    private int id;
    private String type; // Type d'abonnement (mensuel, annuel, etc.)
    private double prix;
    private Date date_debut;
    private Date date_fin;
    private String etat; // Actif, Expiré, Suspendu

    // Constructeurs
    public abonnement() {}

    public abonnement(String type, double prix, Date dateDebut, Date dateFin, String etat) {
        this.type = type;
        this.prix = prix;
        this.date_debut = dateDebut;
        this.date_fin = dateFin;
        this.etat = etat;
    }

    public abonnement(int id, String type, double prix, Date dateDebut, Date dateFin, String etat) {
        this.id = id;
        this.type = type;
        this.prix = prix;
        this.date_debut = dateDebut;
        this.date_fin = dateFin;
        this.etat = etat;
    }

    // Getters et Setters
    public int getid() { return id; }
    public void setid(int id) { this.id = id; }

    public String gettype() { return type; }
    public void settype(String type) { this.type = type; }

    public double getprix() { return prix; }
    public void setprix(double prix) { this.prix = prix; }

    public Date getdate_debut() { return date_debut; }
    public void setdate_debut(Date dateDebut) { this.date_debut = dateDebut; }

    public Date getdate_fin() { return date_fin; }
    public void setdate_fin(Date dateFin) { this.date_fin = dateFin; }

    public String getetat() { return etat; }
    public void setetat(String etat) { this.etat = etat; }

    // Méthode d'affichage
    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", prix=" + prix +
                ", dateDebut=" + date_debut +
                ", dateFin=" + date_fin +
                ", etat='" + etat + '\'' +
                '}';
    }

    // Méthode equals pour comparer deux abonnements
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        abonnement that = (abonnement) o;
        return id == that.id &&
                Double.compare(that.prix, prix) == 0 &&
                Objects.equals(type, that.type) &&
                Objects.equals(date_debut, that.date_debut) &&
                Objects.equals(date_fin, that.date_fin) &&
                Objects.equals(etat, that.etat);
    }

    // Méthode hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, type, prix, date_debut, date_fin, etat);
    }


}
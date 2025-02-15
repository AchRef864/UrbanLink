package tn.esprit.jdbc.entities;

import java.util.Date;
import java.util.Objects;

public class Avis {
    private int avisId;
    private int note;
    private String commentaire;
    private Date dateAvis;
    private int userId;

    public Avis() {}

    public Avis(int note, String commentaire, Date dateAvis, int userId) {
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
        this.userId = userId;
    }

    public Avis(int avisId, int note, String commentaire, Date dateAvis, int userId) {
        this.avisId = avisId;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
        this.userId = userId;
    }

    public int getAvisId() {
        return avisId;
    }

    public void setAvisId(int avisId) {
        this.avisId = avisId;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateAvis() {
        return dateAvis;
    }

    public void setDateAvis(Date dateAvis) {
        this.dateAvis = dateAvis;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "avisId=" + avisId +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                ", dateAvis=" + dateAvis +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Avis avis)) return false;
        if (!super.equals(object)) return false;

        if (avisId != avis.avisId) return false;
        if (note != avis.note) return false;
        if (userId != avis.userId) return false;
        if (!Objects.equals(commentaire, avis.commentaire)) return false;
        return Objects.equals(dateAvis, avis.dateAvis);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + avisId;
        result = 31 * result + note;
        result = 31 * result + (commentaire != null ? commentaire.hashCode() : 0);
        result = 31 * result + (dateAvis != null ? dateAvis.hashCode() : 0);
        result = 31 * result + userId;
        return result;
    }
}
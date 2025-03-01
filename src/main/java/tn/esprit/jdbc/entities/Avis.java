package tn.esprit.jdbc.entities;

import java.util.Date;
import java.util.Objects;

public class Avis {
    private int avis_id;
    private int note;
    private String commentaire;
    private Date date_avis;
    private int user_id;

    public Avis() {}

    public Avis(int note, String commentaire, Date date_avis, int user_id) {
        this.note = note;
        this.commentaire = commentaire;
        this.date_avis = date_avis;
        this.user_id = user_id;
    }

    public Avis(int avis_id, int note, String commentaire, Date date_avis, int user_id) {
        this.avis_id = avis_id;
        this.note = note;
        this.commentaire = commentaire;
        this.date_avis = date_avis;
        this.user_id = user_id;
    }

    public int getAvis_id() {
        return avis_id;
    }

    public void setAvis_id(int avis_id) {
        this.avis_id = avis_id;
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

    public Date getDate_avis() {
        return date_avis;
    }

    public void setDate_avis(Date date_avis) {
        this.date_avis = date_avis;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "avis_id=" + avis_id +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                ", date_avis=" + date_avis +
                ", user_id=" + user_id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Avis avis)) return false;
        if (!super.equals(object)) return false;

        if (avis_id != avis.avis_id) return false;
        if (note != avis.note) return false;
        if (user_id != avis.user_id) return false;
        if (!Objects.equals(commentaire, avis.commentaire)) return false;
        return Objects.equals(date_avis, avis.date_avis);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + avis_id;
        result = 31 * result + note;
        result = 31 * result + (commentaire != null ? commentaire.hashCode() : 0);
        result = 31 * result + (date_avis != null ? date_avis.hashCode() : 0);
        result = 31 * result + user_id;
        return result;
    }
}
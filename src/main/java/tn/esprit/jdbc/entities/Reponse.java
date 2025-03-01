package tn.esprit.jdbc.entities;

import java.util.Date;
import java.util.Objects;

public class Reponse {
    private int reponse_id;
    private String commentaire;
    private Date date_reponse;
    private int avis_id;
    private int user_id;

    public Reponse() {}

    public Reponse(String commentaire, Date date_reponse, int avis_id, int user_id) {
        this.commentaire = commentaire;
        this.date_reponse = date_reponse;
        this.avis_id = avis_id;
        this.user_id = user_id;
    }

    public Reponse(int reponse_id, String commentaire, Date date_reponse, int avis_id, int user_id) {
        this.reponse_id = reponse_id;
        this.commentaire = commentaire;
        this.date_reponse = date_reponse;
        this.avis_id = avis_id;
        this.user_id = user_id;
    }

    public int getReponse_id() {
        return reponse_id;
    }

    public void setReponse_id(int reponse_id) {
        this.reponse_id = reponse_id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDate_reponse() {
        return date_reponse;
    }

    public void setDate_reponse(Date date_reponse) {
        this.date_reponse = date_reponse;
    }

    public int getAvis_id() {
        return avis_id;
    }

    public void setAvis_id(int avis_id) {
        this.avis_id = avis_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "reponse_id=" + reponse_id +
                ", commentaire='" + commentaire + '\'' +
                ", date_reponse=" + date_reponse +
                ", avis_id=" + avis_id +
                ", user_id=" + user_id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Reponse reponse)) return false;
        if (!super.equals(object)) return false;

        if (reponse_id != reponse.reponse_id) return false;
        if (avis_id != reponse.avis_id) return false;
        if (user_id != reponse.user_id) return false;
        if (!Objects.equals(commentaire, reponse.commentaire)) return false;
        return Objects.equals(date_reponse, reponse.date_reponse);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + reponse_id;
        result = 31 * result + (commentaire != null ? commentaire.hashCode() : 0);
        result = 31 * result + (date_reponse != null ? date_reponse.hashCode() : 0);
        result = 31 * result + avis_id;
        result = 31 * result + user_id;
        return result;
    }
}
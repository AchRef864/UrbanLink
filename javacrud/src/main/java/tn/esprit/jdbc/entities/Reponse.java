package tn.esprit.jdbc.entities;

import java.util.Date;
import java.util.Objects;

public class Reponse {
    private int reponseId;
    private String commentaire;
    private Date dateReponse;
    private int avisId;
    private int userId;

    public Reponse() {}

    public Reponse(String commentaire, Date dateReponse, int avisId, int userId) {
        this.commentaire = commentaire;
        this.dateReponse = dateReponse;
        this.avisId = avisId;
        this.userId = userId;
    }

    public Reponse(int reponseId, String commentaire, Date dateReponse, int avisId, int userId) {
        this.reponseId = reponseId;
        this.commentaire = commentaire;
        this.dateReponse = dateReponse;
        this.avisId = avisId;
        this.userId = userId;
    }

    public int getReponseId() {
        return reponseId;
    }

    public void setReponseId(int reponseId) {
        this.reponseId = reponseId;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Date dateReponse) {
        this.dateReponse = dateReponse;
    }

    public int getAvisId() {
        return avisId;
    }

    public void setAvisId(int avisId) {
        this.avisId = avisId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "reponseId=" + reponseId +
                ", commentaire='" + commentaire + '\'' +
                ", dateReponse=" + dateReponse +
                ", avisId=" + avisId +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Reponse reponse)) return false;
        if (!super.equals(object)) return false;

        if (reponseId != reponse.reponseId) return false;
        if (avisId != reponse.avisId) return false;
        if (userId != reponse.userId) return false;
        if (!Objects.equals(commentaire, reponse.commentaire)) return false;
        return Objects.equals(dateReponse, reponse.dateReponse);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + reponseId;
        result = 31 * result + (commentaire != null ? commentaire.hashCode() : 0);
        result = 31 * result + (dateReponse != null ? dateReponse.hashCode() : 0);
        result = 31 * result + avisId;
        result = 31 * result + userId;
        return result;
    }
}

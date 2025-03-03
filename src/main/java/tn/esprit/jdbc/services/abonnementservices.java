package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.abonnement;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class abonnementservices implements CRUD<abonnement> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(abonnement abonnement) throws SQLException {
        String query = "INSERT INTO abonnement (type, prix, date_debut, date_fin, etat) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, abonnement.gettype());
            ps.setDouble(2, abonnement.getprix());
            ps.setDate(3, new java.sql.Date(abonnement.getdate_debut().getTime()));
            ps.setDate(4, new java.sql.Date(abonnement.getdate_fin().getTime()));
            ps.setString(5, abonnement.getetat());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    abonnement.setid(rs.getInt(1)); // Récupérer et définir l'ID généré
                }
            }
            return rowsAffected;
        }
    }

    @Override
    public int update(abonnement abonnement) throws SQLException {
        String query = "UPDATE abonnement SET type = ?, prix = ?, date_debut = ?, date_fin = ?, etat = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, abonnement.gettype());
            ps.setDouble(2, abonnement.getprix());
            ps.setDate(3, new java.sql.Date(abonnement.getdate_debut().getTime()));
            ps.setDate(4, new java.sql.Date(abonnement.getdate_fin().getTime()));
            ps.setString(5, abonnement.getetat());
            ps.setInt(6, abonnement.getid());

            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        String query = "DELETE FROM abonnement WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id); // Utiliser l'ID passé en paramètre
            return ps.executeUpdate();
        }
    }

    @Override
    public List<abonnement> showAll() throws SQLException {
        List<abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";
        try (Statement st = cnx.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                abonnement abonnement = new abonnement();
                abonnement.setid(rs.getInt("id"));
                abonnement.settype(rs.getString("type"));
                abonnement.setprix(rs.getDouble("prix"));
                abonnement.setdate_debut(rs.getDate("date_debut"));
                abonnement.setdate_fin(rs.getDate("date_fin"));
                abonnement.setetat(rs.getString("etat"));
                abonnements.add(abonnement);
            }
        }
        return abonnements;
    }
}
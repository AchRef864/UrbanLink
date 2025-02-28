package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements CRUD<Reclamation> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private PreparedStatement ps;

    @Override
    public int insert(Reclamation reclamation) throws SQLException {
        String req = "INSERT INTO `reclamation` (`user_id`, `sujet`, `description`) VALUES (?, ?, ?)";

        ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, reclamation.getUserId());
        ps.setString(2, reclamation.getSujet());
        ps.setString(3, reclamation.getDescription());

        int rowsAffected = ps.executeUpdate();

        // Retrieve the auto-generated reclamation_id
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                reclamation.setReclamationId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating reclamation failed, no ID obtained.");
            }
        }

        return rowsAffected;
    }

    @Override
    public int update(Reclamation reclamation) throws SQLException {
        String req = "UPDATE `reclamation` SET `sujet` = ?, `description` = ?, `reponse_reclamation` = ? WHERE `reclamation_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, reclamation.getSujet());
        ps.setString(2, reclamation.getDescription());
        ps.setString(3, reclamation.getReponseReclamation());
        ps.setInt(4, reclamation.getReclamationId());
        return ps.executeUpdate();
    }

    @Override
    public int delete(int reclamationId) throws SQLException {
        String req = "DELETE FROM `reclamation` WHERE `reclamation_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, reclamationId);
        return ps.executeUpdate();
    }

    @Override
    public List<Reclamation> showAll() throws SQLException {
        List<Reclamation> temp = new ArrayList<>();

        String req = "SELECT * FROM `reclamation`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Reclamation reclamation = new Reclamation();
            reclamation.setReclamationId(rs.getInt("reclamation_id"));
            reclamation.setUserId(rs.getInt("user_id"));
            reclamation.setSujet(rs.getString("sujet"));
            reclamation.setDescription(rs.getString("description"));
            reclamation.setDateReclamation(rs.getTimestamp("date_reclamation"));
            reclamation.setReponseReclamation(rs.getString("reponse_reclamation"));

            temp.add(reclamation);
        }

        return temp;
    }

    // Optional: Fetch reclamations by user ID
    public List<Reclamation> getReclamationsByUserId(int userId) throws SQLException {
        List<Reclamation> temp = new ArrayList<>();

        String req = "SELECT * FROM `reclamation` WHERE `user_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Reclamation reclamation = new Reclamation();
            reclamation.setReclamationId(rs.getInt("reclamation_id"));
            reclamation.setUserId(rs.getInt("user_id"));
            reclamation.setSujet(rs.getString("sujet"));
            reclamation.setDescription(rs.getString("description"));
            reclamation.setDateReclamation(rs.getTimestamp("date_reclamation"));
            reclamation.setReponseReclamation(rs.getString("reponse_reclamation"));

            temp.add(reclamation);
        }

        return temp;
    }

    public Reclamation getLastReclamationByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM reclamation WHERE user_id = ? ORDER BY date_reclamation DESC LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setReclamationId(rs.getInt("reclamation_id"));
                reclamation.setUserId(rs.getInt("user_id"));
                reclamation.setSujet(rs.getString("sujet"));
                reclamation.setDescription(rs.getString("description"));
                reclamation.setDateReclamation(rs.getTimestamp("date_reclamation"));
                reclamation.setReponseReclamation(rs.getString("reponse_reclamation"));
                return reclamation;
            }
        }
        return null; // No reclamation found
    }
}
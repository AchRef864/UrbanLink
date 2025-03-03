package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.utils.MyDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements CRUD<Avis> {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    @Override
    public int insert(Avis avis) throws SQLException {
        String req = "INSERT INTO `avis`(`note`, `commentaire`, `date_avis`, `user_id`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, avis.getNote());
            ps.setString(2, avis.getCommentaire());
            ps.setDate(3, new java.sql.Date(avis.getDate_avis().getTime()));
            ps.setInt(4, avis.getUser_id());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        avis.setAvis_id(generatedKeys.getInt(1));
                    }
                }
            }
            return rowsAffected;
        }
    }

    @Override
    public int update(Avis avis) throws SQLException {
        String query = "UPDATE avis SET note = ?, commentaire = ?, date_avis = ?, user_id = ? WHERE avis_id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, avis.getNote());
            preparedStatement.setString(2, avis.getCommentaire());
            preparedStatement.setDate(3, new java.sql.Date(avis.getDate_avis().getTime()));
            preparedStatement.setInt(4, avis.getUser_id());
            preparedStatement.setInt(5, avis.getAvis_id());
            return preparedStatement.executeUpdate();
        }
    }


    public int delete(Avis avis) throws SQLException {
        String req = "DELETE FROM avis WHERE avis_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, avis.getAvis_id());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(int userId) throws SQLException {
        return 0;
    }

    @Override
    public List<Avis> showAll() throws SQLException {
        List<Avis> temp = new ArrayList<>();
        String req = "SELECT * FROM `avis`";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Avis a = new Avis();
                a.setAvis_id(rs.getInt("avis_id"));
                a.setNote(rs.getInt("note"));
                a.setCommentaire(rs.getString("commentaire"));
                a.setDate_avis(rs.getDate("date_avis"));
                a.setUser_id(rs.getInt("user_id"));
                temp.add(a);
            }
        }
        return temp;
    }

    public Avis findById(int avisId) throws SQLException {
        String query = "SELECT * FROM avis WHERE avis_id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, avisId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Avis avis = new Avis();
                    avis.setAvis_id(rs.getInt("avis_id"));
                    avis.setNote(rs.getInt("note"));
                    avis.setCommentaire(rs.getString("commentaire"));
                    avis.setDate_avis(rs.getDate("date_avis"));
                    avis.setUser_id(rs.getInt("user_id"));
                    return avis;
                }
            }
        }
        return null;
    }

    public List<Avis> showAllByUserId(int userId) throws SQLException {
        List<Avis> temp = new ArrayList<>();
        String req = "SELECT * FROM `avis` WHERE `user_id` = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Avis a = new Avis();
                    a.setAvis_id(rs.getInt("avis_id"));
                    a.setNote(rs.getInt("note"));
                    a.setCommentaire(rs.getString("commentaire"));
                    a.setDate_avis(rs.getDate("date_avis"));
                    a.setUser_id(rs.getInt("user_id"));
                    temp.add(a);
                }
            }
        }
        return temp;
    }
}
package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Avis;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements CRUD<Avis> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Avis avis) throws SQLException {
        String req = "INSERT INTO `avis`(`note`, `commentaire`, `date_avis`, `user_id`) " +
                "VALUES (?, ?, ?, ?)";
        try {
            ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
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
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public int update(Avis avis) throws SQLException {
        String req = "UPDATE `avis` SET `note` = ?, `commentaire` = ?, `date_avis` = ? WHERE `avis_id` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, avis.getNote());
            ps.setString(2, avis.getCommentaire());
            ps.setDate(3, new java.sql.Date(avis.getDate_avis().getTime()));
            ps.setInt(4, avis.getAvis_id());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public int delete(Avis avis) throws SQLException {
        String req = "DELETE FROM `avis` WHERE `avis_id` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, avis.getAvis_id());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public List<Avis> showAll() throws SQLException {
        List<Avis> temp = new ArrayList<>();
        String req = "SELECT * FROM `avis`";
        try {
            st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Avis a = new Avis();
                a.setAvis_id(rs.getInt("avis_id"));
                a.setNote(rs.getInt("note"));
                a.setCommentaire(rs.getString("commentaire"));
                a.setDate_avis(rs.getDate("date_avis"));
                a.setUser_id(rs.getInt("user_id"));
                temp.add(a);
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return temp;
    }
}
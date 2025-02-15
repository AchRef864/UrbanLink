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
        String req = "INSERT INTO `avis`(`note`, `commentaire`, `dateAvis`, `userId`) " +
                "VALUES (?, ?, ?, ?)";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, avis.getNote());
            ps.setString(2, avis.getCommentaire());
            ps.setDate(3, new java.sql.Date(avis.getDateAvis().getTime()));
            ps.setInt(4, avis.getUserId());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public int update(Avis avis) throws SQLException {
        String req = "UPDATE `avis` SET `note` = ?, `commentaire` = ?, `dateAvis` = ? WHERE `avisId` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, avis.getNote());
            ps.setString(2, avis.getCommentaire());
            ps.setDate(3, new java.sql.Date(avis.getDateAvis().getTime()));
            ps.setInt(4, avis.getAvisId());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public int delete(Avis avis) throws SQLException {
        String req = "DELETE FROM `avis` WHERE `avisId` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, avis.getAvisId());
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
                a.setAvisId(rs.getInt("avisId"));
                a.setNote(rs.getInt("note"));
                a.setCommentaire(rs.getString("commentaire"));
                a.setDateAvis(rs.getDate("dateAvis"));
                a.setUserId(rs.getInt("userId"));
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
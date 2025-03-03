package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements CRUD<Reponse> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Reponse reponse) throws SQLException {
        String req = "INSERT INTO `reponse`(`commentaire`, `date_reponse`, `avis_id`, `user_id`) " +
                "VALUES (?, ?, ?, ?)";
        try {
            ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reponse.getCommentaire());
            ps.setDate(2, new java.sql.Date(reponse.getDate_reponse().getTime()));
            ps.setInt(3, reponse.getAvis_id());
            ps.setInt(4, reponse.getUser_id());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reponse.setReponse_id(generatedKeys.getInt(1));
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
    public int update(Reponse reponse) throws SQLException {
        String req = "UPDATE `reponse` SET `commentaire` = ? WHERE `reponse_id` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setString(1, reponse.getCommentaire());
            ps.setInt(2, reponse.getReponse_id());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public int delete(Reponse reponse) throws SQLException {
        String req = "DELETE FROM `reponse` WHERE `reponse_id` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, reponse.getReponse_id());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public int delete(int userId) throws SQLException {
        return 0;
    }

    @Override
    public List<Reponse> showAll() throws SQLException {
        List<Reponse> temp = new ArrayList<>();
        String req = "SELECT * FROM `reponse`";
        try {
            st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setReponse_id(rs.getInt("reponse_id"));
                r.setCommentaire(rs.getString("commentaire"));
                r.setDate_reponse(rs.getDate("date_reponse"));
                r.setAvis_id(rs.getInt("avis_id"));
                r.setUser_id(rs.getInt("user_id"));
                temp.add(r);
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return temp;
    }

    public List<Reponse> getReponsesByAvisId(int avisId) throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String req = "SELECT * FROM `reponse` WHERE `avis_id` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, avisId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setReponse_id(rs.getInt("reponse_id"));
                r.setCommentaire(rs.getString("commentaire"));
                r.setDate_reponse(rs.getDate("date_reponse"));
                r.setAvis_id(rs.getInt("avis_id"));
                r.setUser_id(rs.getInt("user_id"));
                reponses.add(r);
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return reponses;
    }
}
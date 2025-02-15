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
        String req = "INSERT INTO `reponse`(`commentaire`, `dateReponse`, `avisId`, `userId`) " +
                "VALUES (?, ?, ?, ?)";
        try {
            ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reponse.getCommentaire());
            ps.setDate(2, new java.sql.Date(reponse.getDateReponse().getTime()));
            ps.setInt(3, reponse.getAvisId());
            ps.setInt(4, reponse.getUserId());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reponse.setReponseId(generatedKeys.getInt(1));
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
        String req = "UPDATE `reponse` SET `commentaire` = ?, `dateReponse` = ? WHERE `reponseId` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setString(1, reponse.getCommentaire());
            ps.setDate(2, new java.sql.Date(reponse.getDateReponse().getTime()));
            ps.setInt(3, reponse.getReponseId());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    @Override
    public int delete(Reponse reponse) throws SQLException {
        String req = "DELETE FROM `reponse` WHERE `reponseId` = ?";
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, reponse.getReponseId());
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
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
                r.setReponseId(rs.getInt("reponseId"));
                r.setCommentaire(rs.getString("commentaire"));
                r.setDateReponse(rs.getDate("dateReponse"));
                r.setAvisId(rs.getInt("avisId"));
                r.setUserId(rs.getInt("userId"));
                temp.add(r);
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return temp;
    }
}
package services;

import entity.abonnement;
import utiles.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class abonnementservices implements CRUD<abonnement> {

    private Connection connection = MyDataBase.getInstance().getConnection();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(abonnement abonnement) throws SQLException {
        String query = "INSERT INTO abonnement (type, prix, date_debut, date_fin, etat) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
        try (PreparedStatement ps = connection.prepareStatement(query)) {
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
    public int delete(abonnement abonnement) throws SQLException {
        String query = "DELETE FROM abonnement WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, abonnement.getid());
            return ps.executeUpdate();
        }
    }

    @Override
    public List<abonnement> showAll() throws SQLException {
        List<abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";
        try (Statement st = connection.createStatement()) {
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

    // New method to get all abonnements
    public List<abonnement> getAll() {
        List<abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                double prix = resultSet.getDouble("prix");
                Date date_debut = resultSet.getDate("date_debut");
                Date date_fin = resultSet.getDate("date_fin");
                String etat = resultSet.getString("etat");

                // Create a new abonnement object and add it to the list
                abonnement abonnement = new abonnement(id, type, prix, date_debut, date_fin, etat);
                abonnements.add(abonnement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return abonnements;
    }
}
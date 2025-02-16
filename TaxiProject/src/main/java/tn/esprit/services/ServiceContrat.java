package tn.esprit.services;

import tn.esprit.models.Contrat;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceContrat implements CrudService<Contrat> {

    private final Connection connection;

    public ServiceContrat() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Contrat contrat) throws SQLException {
        String query = "INSERT INTO contrat (id_taxi, date_debut, date_fin, montant, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, contrat.getId_taxi());
            pstmt.setDate(2, Date.valueOf(contrat.getDate_debut()));
            pstmt.setDate(3, Date.valueOf(contrat.getDate_fin()));
            pstmt.setDouble(4, contrat.getMontant());
            pstmt.setString(5, contrat.getStatut());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contrat.setId_contrat(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(Contrat contrat) throws SQLException {
        String query = "UPDATE contrat SET id_taxi=?, date_debut=?, date_fin=?, montant=?, statut=? WHERE id_contrat=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, contrat.getId_taxi());
            pstmt.setDate(2, Date.valueOf(contrat.getDate_debut()));
            pstmt.setDate(3, Date.valueOf(contrat.getDate_fin()));
            pstmt.setDouble(4, contrat.getMontant());
            pstmt.setString(5, contrat.getStatut());
            pstmt.setInt(6, contrat.getId_contrat());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM contrat WHERE id_contrat=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Contrat> afficher() throws SQLException {
        List<Contrat> contrats = new ArrayList<>();
        String query = "SELECT * FROM contrat";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Contrat contrat = new Contrat(
                        rs.getInt("id_contrat"),
                        rs.getInt("id_taxi"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getDouble("montant"),
                        rs.getString("statut")
                );
                contrats.add(contrat);
            }
        }
        return contrats;
    }
}
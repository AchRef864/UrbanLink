package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Taxi;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTaxi implements CRUD<Taxi> {
    private final Connection connection;

    public ServiceTaxi() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    public boolean licenceExiste(String licenceNumero, Integer idExclu) throws SQLException {
        String query = "SELECT COUNT(*) FROM taxi WHERE licence_numero = ?" +
                (idExclu != null ? " AND id_taxi != ?" : "");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, licenceNumero);
            if (idExclu != null) {
                pstmt.setInt(2, idExclu);
            }
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public boolean immatriculationExiste(String immatriculation, Integer idExclu) throws SQLException {
        String query = "SELECT COUNT(*) FROM taxi WHERE immatriculation = ?" +
                (idExclu != null ? " AND id_taxi != ?" : "");
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, immatriculation);
            if (idExclu != null) {
                pstmt.setInt(2, idExclu);
            }
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public int insert(Taxi taxi) throws SQLException {
        if (licenceExiste(taxi.getLicenceNumero(), null)) {
            throw new SQLException("Le numéro de licence existe déjà !");
        }
        if (immatriculationExiste(taxi.getImmatriculation(), null)) {
            throw new SQLException("L'immatriculation existe déjà !");
        }
        String query = "INSERT INTO taxi (immatriculation, marque, modele, annee_fabrication, capacite, zone_desserte, statut, licence_numero, licence_date_obtention, tarif_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setTaxiParameters(pstmt, taxi);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    taxi.setIdTaxi(generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Échec de l'insertion, aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'ajout du taxi : " + e.getMessage());
        }
    }

    @Override
    public int update(Taxi taxi) throws SQLException {
        if (licenceExiste(taxi.getLicenceNumero(), taxi.getIdTaxi())) {
            throw new SQLException("Le numéro de licence existe déjà !");
        }
        if (immatriculationExiste(taxi.getImmatriculation(), taxi.getIdTaxi())) {
            throw new SQLException("L'immatriculation existe déjà !");
        }
        String query = "UPDATE taxi SET immatriculation=?, marque=?, modele=?, annee_fabrication=?, capacite=?, zone_desserte=?, statut=?, licence_numero=?, licence_date_obtention=?, tarif_base=? WHERE id_taxi=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            setTaxiParameters(pstmt, taxi);
            pstmt.setInt(11, taxi.getIdTaxi());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la modification du taxi : " + e.getMessage());
        }
    }

    private void setTaxiParameters(PreparedStatement pstmt, Taxi taxi) throws SQLException {
        pstmt.setString(1, taxi.getImmatriculation());
        pstmt.setString(2, taxi.getMarque());
        pstmt.setString(3, taxi.getModele());
        pstmt.setInt(4, taxi.getAnneeFabrication());
        pstmt.setInt(5, taxi.getCapacite());
        pstmt.setString(6, taxi.getZoneDesserte());
        pstmt.setString(7, taxi.getStatut());
        pstmt.setString(8, taxi.getLicenceNumero());
        pstmt.setDate(9, Date.valueOf(taxi.getLicenceDateObtention()));
        pstmt.setDouble(10, taxi.getTarifBase());
    }

    @Override
    public int delete(int id) throws SQLException {
        String query = "DELETE FROM taxi WHERE id_taxi=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la suppression du taxi : " + e.getMessage());
        }
    }

    @Override
    public List<Taxi> showAll() throws SQLException {
        List<Taxi> taxis = new ArrayList<>();
        String query = "SELECT * FROM taxi";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Taxi taxi = new Taxi(
                        rs.getInt("id_taxi"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee_fabrication"),
                        rs.getInt("capacite"),
                        rs.getString("zone_desserte"),
                        rs.getString("statut"),
                        rs.getString("licence_numero"),
                        rs.getDate("licence_date_obtention").toLocalDate(),
                        rs.getDouble("tarif_base")
                );
                taxis.add(taxi);
            }
        }
        return taxis;
    }

    public List<Taxi> getAllTaxis() throws SQLException {
        return showAll();
    }

    // Méthode pour récupérer un taxi via son identifiant
    public Taxi getTaxiById(int id) throws SQLException {
        String query = "SELECT * FROM taxi WHERE id_taxi = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new Taxi(
                        rs.getInt("id_taxi"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee_fabrication"),
                        rs.getInt("capacite"),
                        rs.getString("zone_desserte"),
                        rs.getString("statut"),
                        rs.getString("licence_numero"),
                        rs.getDate("licence_date_obtention").toLocalDate(),
                        rs.getDouble("tarif_base")
                );
            }
        }
        return null;
    }
}

package tn.esprit.services;

import tn.esprit.models.Taxi;
import tn.esprit.utils.MyDataBase;
import tn.esprit.services.CrudService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTaxi implements CrudService<Taxi> {
    private final Connection connection;

    public ServiceTaxi() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    // Vérifie si une licence existe déjà (avec exclusion optionnelle pour la modification)
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

    // Vérifie si une immatriculation existe déjà (avec exclusion optionnelle)
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
    public void ajouter(Taxi taxi) throws SQLException {
        // Vérifications avant insertion
        if (licenceExiste(taxi.getLicenceNumero(), null)) {
            throw new SQLException("Le numéro de licence existe déjà !");
        }
        if (immatriculationExiste(taxi.getImmatriculation(), null)) {
            throw new SQLException("L'immatriculation existe déjà !");
        }

        String query = "INSERT INTO taxi (immatriculation, marque, modele, annee_fabrication, capacite, zone_desserte, statut, licence_numero, licence_date_obtention, tarif_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setTaxiParameters(pstmt, taxi);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taxi.setIdTaxi(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'ajout du taxi : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Taxi taxi) throws SQLException {
        // Vérifications avant modification
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
            pstmt.executeUpdate();
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
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM taxi WHERE id_taxi=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Taxi supprimé avec succès !");
        }
    }

    @Override
    public List<Taxi> afficher() throws SQLException {
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
    public Taxi getTaxiById(int id) throws SQLException {
        String query = "SELECT * FROM taxi WHERE id_taxi = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
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
                throw new SQLException("Taxi non trouvé");
            }
        }
    }

    public List<Taxi> getAllTaxis() throws SQLException {
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
}
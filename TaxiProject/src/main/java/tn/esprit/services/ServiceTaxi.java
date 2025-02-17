package tn.esprit.services;

import tn.esprit.models.Taxi;
import tn.esprit.utils.MyDataBase;
import tn.esprit.services.CrudService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTaxi implements CrudService <Taxi> {
    private final Connection connection;

    public ServiceTaxi() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Taxi taxi) throws SQLException {
        String query = "INSERT INTO taxi (immatriculation, marque, modele, annee_fabrication, capacite, zone_desserte, statut, licence_numero, licence_date_obtention, tarif_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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

            pstmt.executeUpdate();

            // 🔍 Récupération de l'ID généré
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                taxi.setIdTaxi(generatedKeys.getInt(1));  // Mise à jour de l'objet Taxi
            }

            System.out.println("Taxi ajouté avec succès ! ID: " + taxi.getIdTaxi());
        }
    }


    @Override
    public void modifier(Taxi taxi) throws SQLException {
        String query = "UPDATE taxi SET immatriculation=?, marque=?, modele=?, annee_fabrication=?, capacite=?, zone_desserte=?, statut=?, licence_numero=?, licence_date_obtention=?, tarif_base=? WHERE id_taxi=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
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
            pstmt.setInt(11, taxi.getIdTaxi());

            pstmt.executeUpdate();
            System.out.println("Taxi mis à jour !");
        }
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
}
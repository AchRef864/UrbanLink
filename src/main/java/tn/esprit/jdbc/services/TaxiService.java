package tn.esprit.jdbc.services;

import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.jdbc.entities.Taxii;

public class TaxiService {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    public List<String> getAllTaxiImmatriculations() throws SQLException {
        List<String> immatriculations = new ArrayList<>();
        String query = "SELECT immatriculation FROM taxi";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                immatriculations.add(rs.getString("immatriculation"));
            }
        }
        return immatriculations;
    }

    public int getTaxiIdByImmatriculation(String immatriculation) throws SQLException {
        String query = "SELECT id_taxi FROM taxi WHERE immatriculation = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, immatriculation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_taxi");
            }
        }
        return -1; // Return -1 if no matching taxi is found
    }

    public List<Taxii> getAllTaxis() throws SQLException {
        List<Taxii> taxis = new ArrayList<>();
        String query = "SELECT * FROM taxi";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Taxii taxii = new Taxii(
                        rs.getInt("id_taxi"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee_fabrication"),
                        rs.getInt("capacite"),
                        rs.getString("zone_desserte"),
                        rs.getString("licence_numero"),
                        rs.getDate("licence_date_obtention"),
                        rs.getDouble("tarif_base"),
                        rs.getInt("user_id"),
                        rs.getInt("maintenance_id")
                );
                taxis.add(taxii);
            }
        }
        return taxis;
    }

    public int countTaxis() throws SQLException {
        String query = "SELECT COUNT(*) AS taxi_count FROM taxi";
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("taxi_count");
            }
        }
        return 0;
    }
}
package tn.esprit.jdbc.services;

import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxiService {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    // Get all taxi immatriculations
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
}
package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.*;
import tn.esprit.jdbc.services.CRUD;
import tn.esprit.jdbc.utils.MyDatabase ;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class reservationservice implements CRUD<reservation> {
    private Connection cnx;

    public reservationservice() {
        cnx = MyDatabase.getInstance().getCnx(); // solve error :
        // java.sql.SQLException: No suitable driver found for jdbc:mysql://localhost:3306/pidev
    }

    @Override
    public int insert(reservation reservation) throws SQLException {
        String query = "INSERT INTO abonnement_reservation (abonnement_id, user_id, statut, dateDebut, dateFin) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, reservation.getabonnement().getid()); // Abonnement ID first
        ps.setInt(2, reservation.getuserId()); // User ID second
        ps.setString(3, reservation.getstatut()); // Statut third
        ps.setDate(4, new java.sql.Date(reservation.getdateDebut().getTime())); // dateDebut fourth
        ps.setDate(5, new java.sql.Date(reservation.getdateFin().getTime())); // dateFin last

        int rowsInserted = ps.executeUpdate();
        if (rowsInserted > 0) {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated key (ID of the reservation)
            }
        }
        return -1;
    }

    @Override
    public int update(reservation reservation) throws SQLException {
        String query = "UPDATE abonnement_reservation SET abonnement_id = ?, user_id = ?, statut = ?, dateDebut = ?, dateFin = ? WHERE abonnement_reservation_id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reservation.getabonnement().getid());
        ps.setInt(2, reservation.getuserId());
        ps.setString(3, reservation.getstatut());
        ps.setDate(4, new java.sql.Date(reservation.getdateDebut().getTime()));
        ps.setDate(5, new java.sql.Date(reservation.getdateFin().getTime()));
        ps.setInt(6, reservation.getId()); // Update based on the reservation ID

        return ps.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }



    @Override
    public List<reservation> showAll() throws SQLException {
        List<reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, a.type FROM abonnement_reservation r " +
                "JOIN abonnement a ON r.abonnement_id = a.id";

        Statement stmt = cnx.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            abonnement abonnement = new abonnement();
            abonnement.setid(rs.getInt("abonnement_id"));
            abonnement.settype(rs.getString("type")); // Récupérer le type d'abonnement directement

            reservation res = new reservation(
                    rs.getInt("abonnement_reservation_id"),
                    rs.getInt("user_id"),
                    rs.getDate("dateDebut"),
                    rs.getDate("dateFin"),
                    rs.getString("statut"),
                    abonnement
            );
            reservations.add(res);
        }
        System.out.println("BackEnd "+reservations);
        return reservations;
    }


}

package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.abonnement;
import tn.esprit.jdbc.entities.reservation;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class reservationservice implements CRUD<reservation> {
    private Connection cnx;

    public reservationservice() {
        cnx = MyDatabase.getInstance().getCnx();
        if (cnx == null) {
            throw new RuntimeException("La connexion à la base de données n'a pas pu être établie.");
        }
    }

    @Override
    public int insert(reservation reservation) throws SQLException {
        String query = "INSERT INTO abonnement_reservation (abonnement_id, user_id, statut, dateDebut, dateFin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reservation.getabonnement().getid());
            ps.setInt(2, reservation.getuserId());
            ps.setString(3, reservation.getstatut());
            ps.setDate(4, new java.sql.Date(reservation.getdateDebut().getTime()));
            ps.setDate(5, new java.sql.Date(reservation.getdateFin().getTime()));

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    System.out.println("Inserted reservation with ID: " + generatedId);
                    return generatedId;
                }
            }
            System.out.println("Failed to insert reservation: No rows affected.");
            return -1;
        } catch (SQLException e) {
            System.err.println("Error inserting reservation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public int update(reservation reservation) throws SQLException {
        String query = "UPDATE abonnement_reservation SET abonnement_id = ?, user_id = ?, statut = ?, dateDebut = ?, dateFin = ? WHERE abonnement_reservation_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, reservation.getabonnement().getid());
            ps.setInt(2, reservation.getuserId());
            ps.setString(3, reservation.getstatut());
            ps.setDate(4, new java.sql.Date(reservation.getdateDebut().getTime()));
            ps.setDate(5, new java.sql.Date(reservation.getdateFin().getTime()));
            ps.setInt(6, reservation.getId());

            int rowsUpdated = ps.executeUpdate();
            System.out.println("Updated reservation with ID " + reservation.getId() + ": " + rowsUpdated + " rows affected.");
            return rowsUpdated;
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        String query = "DELETE FROM abonnement_reservation WHERE abonnement_reservation_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            System.out.println("Deleted reservation with ID " + id + ": " + rowsDeleted + " rows affected.");
            return rowsDeleted;
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<reservation> showAll() throws SQLException {
        List<reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, a.type FROM abonnement_reservation r " +
                "JOIN abonnement a ON r.abonnement_id = a.id";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                abonnement abonnement = new abonnement();
                abonnement.setid(rs.getInt("abonnement_id"));
                abonnement.settype(rs.getString("type"));

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
            System.out.println("Reservations loaded from DB: " + reservations);
            return reservations;
        } catch (SQLException e) {
            System.err.println("Error loading reservations: " + e.getMessage());
            throw e;
        }
    }
}
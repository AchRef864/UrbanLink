package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Reservation;
import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements CRUD<Reservation> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private PreparedStatement ps;

    @Override
    public int insert(Reservation reservation) throws SQLException {
        String req = "INSERT INTO `reservation` (`trajet_id`, `user_id`, `reservation_date`, `number_of_seats`, `total_price`) VALUES (?, ?, ?, ?, ?)";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, reservation.getTrajet().getId()); // Get Trajet ID from Trajet object
        ps.setInt(2, reservation.getUser().getUserId()); // Get user ID from User object
        ps.setDate(3, new java.sql.Date(reservation.getReservationDate().getTime()));
        ps.setInt(4, reservation.getNumberOfSeats());
        ps.setDouble(5, reservation.getTotalPrice());

        return ps.executeUpdate();
    }

    @Override
    public int update(Reservation reservation) throws SQLException {
        String req = "UPDATE `reservation` SET `trajet_id` = ?, `user_id` = ?, `reservation_date` = ?, `number_of_seats` = ?, `total_price` = ? WHERE `reservation_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, reservation.getTrajet().getId()); // Get Trajet ID from Trajet object
        ps.setInt(2, reservation.getUser().getUserId()); // Get user ID from User object
        ps.setDate(3, new java.sql.Date(reservation.getReservationDate().getTime()));
        ps.setInt(4, reservation.getNumberOfSeats());
        ps.setDouble(5, reservation.getTotalPrice());
        ps.setInt(6, reservation.getIdReservation());

        return ps.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String req = "DELETE FROM `reservation` WHERE `reservation_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    @Override
    public List<Reservation> showAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM `reservation`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setIdReservation(rs.getInt("reservation_id"));

            // Fetch associated Trajet
            Trajet trajet = getTrajetById(rs.getInt("trajet_id"));
            reservation.setTrajet(trajet);

            // Fetch associated User
            User user = getUserById(rs.getInt("user_id"));
            reservation.setUser(user);

            reservation.setReservationDate(rs.getDate("reservation_date"));
            reservation.setNumberOfSeats(rs.getInt("number_of_seats"));
            reservation.setTotalPrice(rs.getDouble("total_price"));

            reservations.add(reservation);
        }

        return reservations;
    }

    // Optional: Get reservations by user ID
    public List<Reservation> getReservationsByUserId(int userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM `reservation` WHERE `user_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setIdReservation(rs.getInt("reservation_id"));

            // Fetch associated Trajet
            Trajet trajet = getTrajetById(rs.getInt("trajet_id"));
            reservation.setTrajet(trajet);

            // Fetch associated User
            User user = getUserById(rs.getInt("user_id"));
            reservation.setUser(user);

            reservation.setReservationDate(rs.getDate("reservation_date"));
            reservation.setNumberOfSeats(rs.getInt("number_of_seats"));
            reservation.setTotalPrice(rs.getDouble("total_price"));

            reservations.add(reservation);
        }

        return reservations;
    }

    // Helper method to fetch a User by user_id
    private User getUserById(int userId) throws SQLException {
        String req = "SELECT * FROM `users` WHERE `user_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setPassword(rs.getString("password"));

            return user;
        }
        return null; // User not found
    }

    // Helper method to fetch a Trajet by trajet_id
    private Trajet getTrajetById(int trajetId) throws SQLException {
        String req = "SELECT * FROM `trajet` WHERE `id_trajet` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, trajetId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Trajet trajet = new Trajet();
            trajet.setId(rs.getInt("id_trajet"));
            trajet.setDeparture(rs.getString("departure"));
            trajet.setDestination(rs.getString("destination"));
            trajet.setDistance(rs.getDouble("distance"));
            trajet.setDuration(rs.getString("duration"));
            trajet.setDepartureTime(rs.getString("departure_time"));
            trajet.setArrivalTime(rs.getString("arrival_time"));
            trajet.setPrice(rs.getDouble("price"));

            return trajet;
        }
        return null; // Trajet not found
    }
}

package tn.esprit.jdbc.entities;

import java.util.Date;

public class Reservation {

    private int idReservation;  // Corresponds to reservation_id
    private Trajet trajet;      // Changed from idTrajet to Trajet object
    private User user;          // Reference to User object (instead of userId)
    private Date reservationDate;
    private int numberOfSeats;
    private double totalPrice;

    // Default constructor
    public Reservation() {}

    // Constructor without idReservation (for new reservations)
    public Reservation(Trajet trajet, User user, Date reservationDate, int numberOfSeats, double totalPrice) {
        this.trajet = trajet;
        this.user = user;
        this.reservationDate = reservationDate;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
    }

    // Constructor with idReservation (for existing reservations)
    public Reservation(int idReservation, Trajet trajet, User user, Date reservationDate, int numberOfSeats, double totalPrice) {
        this.idReservation = idReservation;
        this.trajet = trajet;
        this.user = user;
        this.reservationDate = reservationDate;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", trajet=" + trajet +
                ", user=" + user +
                ", reservationDate=" + reservationDate +
                ", numberOfSeats=" + numberOfSeats +
                ", totalPrice=" + totalPrice +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Reservation reservation)) return false;

        if (idReservation != reservation.idReservation) return false;
        if (!trajet.equals(reservation.trajet)) return false;
        if (!user.equals(reservation.user)) return false;
        if (numberOfSeats != reservation.numberOfSeats) return false;
        if (Double.compare(reservation.totalPrice, totalPrice) != 0) return false;
        return reservationDate.equals(reservation.reservationDate);
    }

    @Override
    public int hashCode() {
        int result = idReservation;
        result = 31 * result + trajet.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + reservationDate.hashCode();
        result = 31 * result + numberOfSeats;
        result = 31 * result + Double.hashCode(totalPrice);
        return result;
    }
}

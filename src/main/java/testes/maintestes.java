package testes;

import entity.reservation;
import services.reservationservice;

import java.sql.SQLException;
import java.util.List;

public class maintestes {
    public static void main(String[] args) {
        // Create an instance of reservationservice to interact with the database
        reservationservice reservationService = new reservationservice();

        try {
            // Fetch all reservations from the database
            List<reservation> reservations = reservationService.showAll();

            if (reservations.isEmpty()) {
                System.out.println("Aucune réservation trouvée.");
            } else {
                // Print all reservations
                for (reservation res : reservations) {
                    System.out.println(res); // This will use the toString method of the reservation class
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

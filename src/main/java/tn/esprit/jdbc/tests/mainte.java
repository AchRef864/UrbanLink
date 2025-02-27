package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Reservation;
import tn.esprit.jdbc.services.ReservationService;
import tn.esprit.jdbc.services.TrajetService;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.SQLException;
import java.util.Date;

public class mainte {
    public static void main(String[] args) {

        MyDatabase m1 = MyDatabase.getInstance();

        TrajetService ts = new TrajetService();



        try {


            System.out.println(ts.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    }


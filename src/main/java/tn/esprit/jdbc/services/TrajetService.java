package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.entities.TrajetType;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements CRUD<Trajet> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private PreparedStatement ps;

    @Override
    public int insert(Trajet trajet) {
        String query = "INSERT INTO trajet (departure, destination, distance, duration, departure_time, arrival_time, price, type_transport) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyDatabase.instance.getCnx();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trajet.getDeparture());
            stmt.setString(2, trajet.getDestination());
            stmt.setDouble(3, trajet.getDistance());
            stmt.setString(4, trajet.getDuration());
            stmt.setString(5, trajet.getDepartureTime());
            stmt.setString(6, trajet.getArrivalTime());
            stmt.setDouble(7, trajet.getPrice());
            stmt.setString(8, trajet.getTrajetType().toString());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating trajet failed, no rows affected.");
            }

            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);  // Get the generated ID
                    trajet.setId(generatedId);  // Set the ID to the trajet object
                    return generatedId;  // Return the generated ID
                } else {
                    throw new SQLException("Creating trajet failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // In case of error, return -1 or any other error code
        }
    }

    @Override
    public int update(Trajet trajet) throws SQLException {
        String req = "UPDATE `trajet` SET `departure` = ?, `destination` = ?, `distance` = ?, `duration` = ?, `departure_time` = ?, `arrival_time` = ?, `price` = ?, `type_transport` = ? WHERE `trajet_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, trajet.getDeparture());
        ps.setString(2, trajet.getDestination());
        ps.setDouble(3, trajet.getDistance());
        ps.setString(4, trajet.getDuration()); // Save duration as double
        ps.setString(5, trajet.getDepartureTime());
        ps.setString(6, trajet.getArrivalTime());
        ps.setDouble(7, trajet.getPrice());
        ps.setString(8, trajet.getTrajetType().name()); // Save enum directly as an enum
        ps.setInt(9, trajet.getId());

        return ps.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String req = "DELETE FROM `trajet` WHERE `trajet_id` = ?";
        ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    @Override
    public List<Trajet> showAll() throws SQLException {
        List<Trajet> temp = new ArrayList<>();
        String req = "SELECT * FROM `trajet`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Trajet trajet = new Trajet();
            trajet.setId(rs.getInt("trajet_id"));
            trajet.setDeparture(rs.getString("departure"));
            trajet.setDestination(rs.getString("destination"));
            trajet.setDistance(rs.getDouble("distance"));
            trajet.setDuration(rs.getString("duration")); // Fetch duration as double
            trajet.setDepartureTime(rs.getString("departure_time"));
            trajet.setArrivalTime(rs.getString("arrival_time"));
            trajet.setPrice(rs.getDouble("price"));
            trajet.setTrajetType(TrajetType.valueOf(rs.getString("type_transport"))); // Fetch trajetType as enum
            System.out.println("hi2");
            temp.add(trajet);
        }

        return temp;
    }

    // Optional: Fetch trajets by destination
    public List<Trajet> getTrajetsByDestination(String destination) throws SQLException {
        List<Trajet> temp = new ArrayList<>();
        String req = "SELECT * FROM `trajet` WHERE `destination` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, destination);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Trajet trajet = new Trajet();
            trajet.setId(rs.getInt("trajet_id"));
            trajet.setDeparture(rs.getString("departure"));
            trajet.setDestination(rs.getString("destination"));
            trajet.setDistance(rs.getDouble("distance"));
            trajet.setDuration(rs.getString("duration")); // Fetch duration as double
            trajet.setDepartureTime(rs.getString("departure_time"));
            trajet.setArrivalTime(rs.getString("arrival_time"));
            trajet.setPrice(rs.getDouble("price"));
            trajet.setTrajetType(TrajetType.valueOf(rs.getString("type_transport"))); // Set trajetType from the result set

            temp.add(trajet);
        }

        return temp;
    }

    public int getTrajetId(String departure, String destination, double distance, String duration,
                           String departureTime, String arrivalTime, double price, TrajetType trajetType) {

        String query = "SELECT trajet_id FROM trajet WHERE departure = ? AND destination = ? AND distance = ? AND duration = ? " +
                "AND departure_time = ? AND arrival_time = ? AND price = ? AND type_transport = ?";

        // Do not use try-with-resources for the connection, so that we can keep the connection open
        try (PreparedStatement stmt = MyDatabase.instance.getCnx().prepareStatement(query)) {

            // Set all the parameters
            stmt.setString(1, departure);
            stmt.setString(2, destination);
            stmt.setDouble(3, distance);
            stmt.setString(4, duration);  // Duration as string
            stmt.setString(5, departureTime);
            stmt.setString(6, arrivalTime);
            stmt.setDouble(7, price);
            stmt.setString(8, trajetType.toString());  // Convert TrajetType enum to string

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("trajet_id");  // Return the ID if found
            } else {
                System.out.println("Trajet not found.");
                return -1;  // Return -1 if not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Return -1 in case of error
        }
    }

    public List<Trajet> getAllTrajets() {
        List<Trajet> trajets = new ArrayList<>();
        String query = "SELECT * FROM trajet";

        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String departure = resultSet.getString("departure");
                String destination = resultSet.getString("destination");
                double distance = resultSet.getDouble("distance");
                String duration = resultSet.getString("duration"); // Fetch duration as double
                String departureTime = resultSet.getString("departure_time");
                String arrivalTime = resultSet.getString("arrival_time");
                double price = resultSet.getDouble("price");
                TrajetType trajetType = TrajetType.valueOf(resultSet.getString("type_transport")); // Fetch trajetType

                // Add each trajet to the list
                Trajet trajet = new Trajet(departure, destination, distance, duration, departureTime, arrivalTime, price, trajetType);
                trajets.add(trajet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trajets;
    }
}

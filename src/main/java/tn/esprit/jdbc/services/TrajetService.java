package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Trajet;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements CRUD<Trajet> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private PreparedStatement ps;

    @Override
    public int insert(Trajet trajet) throws SQLException {
        String req = "INSERT INTO `trajet` (`departure`, `destination`, `distance`, `duration`, `departure_time`, `arrival_time`, `price`,`vehicle_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        ps = cnx.prepareStatement(req);
        ps.setString(1, trajet.getDeparture());
        ps.setString(2, trajet.getDestination());
        ps.setDouble(3, trajet.getDistance());
        ps.setString(4, trajet.getDuration());
        ps.setString(5, trajet.getDepartureTime());
        ps.setString(6, trajet.getArrivalTime());
        ps.setDouble(7, trajet.getPrice());


        return ps.executeUpdate();


    }

    @Override
    public int update(Trajet trajet) throws SQLException {
        String req = "UPDATE `trajet` SET `departure` = ?, `destination` = ?, `distance` = ?, `duration` = ?, `departure_time` = ?, `arrival_time` = ?, `price` = ? WHERE `id_trajet` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, trajet.getDeparture());
        ps.setString(2, trajet.getDestination());
        ps.setDouble(3, trajet.getDistance());
        ps.setString(4, trajet.getDuration());
        ps.setString(5, trajet.getDepartureTime());
        ps.setString(6, trajet.getArrivalTime());
        ps.setDouble(7, trajet.getPrice());
        ps.setInt(8, trajet.getId());

        return ps.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String req = "DELETE FROM `trajet` WHERE `id_trajet` = ?";
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
            trajet.setId(rs.getInt("id_trajet"));
            trajet.setDeparture(rs.getString("departure"));
            trajet.setDestination(rs.getString("destination"));
            trajet.setDistance(rs.getDouble("distance"));
            trajet.setDuration(rs.getString("duration"));
            trajet.setDepartureTime(rs.getString("departure_time"));
            trajet.setArrivalTime(rs.getString("arrival_time"));
            trajet.setPrice(rs.getDouble("price"));

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
            trajet.setId(rs.getInt("id"));
            trajet.setDeparture(rs.getString("departure"));
            trajet.setDestination(rs.getString("destination"));
            trajet.setDistance(rs.getDouble("distance"));
            trajet.setDuration(rs.getString("duration"));
            trajet.setDepartureTime(rs.getString("departure_time"));
            trajet.setArrivalTime(rs.getString("arrival_time"));
            trajet.setPrice(rs.getDouble("price"));

            temp.add(trajet);
        }

        return temp;
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
                String duration = resultSet.getString("duration");
                String departureTime = resultSet.getString("departure_time");
                String arrivalTime = resultSet.getString("arrival_time");
                double price = resultSet.getDouble("price");

                // Ajouter chaque trajet à la liste
                Trajet trajet = new Trajet(departure, destination, distance, duration, departureTime, arrivalTime, price);
                trajets.add(trajet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trajets;
    }


}

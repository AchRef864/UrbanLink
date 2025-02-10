package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Person;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonService implements CRUD<Person> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(Person person) throws SQLException {

        String req = "INSERT INTO `person`(`firstname`, `lastname`, `age`) " +
                "VALUES ('"+person.getFirstName()+"','"+person.getLastName()+"', "+person.getAge()+")";

        st = cnx.createStatement();

        return st.executeUpdate(req);
    }

    public int insert1(Person person) throws SQLException {

        String req = "INSERT INTO `person`(`firstname`, `lastname`, `age`) VALUES (?, ?, ?)";

        ps = cnx.prepareStatement(req);

        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setInt(3, person.getAge());

        return ps.executeUpdate();
    }

    @Override
    public int update(Person person) throws SQLException{
        return 0;
    }

    @Override
    public int delete(Person person) throws SQLException{
        return 0;
    }

    @Override
    public List<Person> showAll() throws SQLException{
        List<Person> temp = new ArrayList<>();

        String req = "SELECT * FROM `person`";

        st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Person p = new Person();
            p.setId(rs.getInt(1));
            p.setFirstName(rs.getString("firstname"));
            p.setLastName(rs.getString(3));
            p.setAge(rs.getInt("age"));

            temp.add(p);
        }

        return temp;
    }
}

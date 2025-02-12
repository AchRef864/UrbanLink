package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.User;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements CRUD<User> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(User user) throws SQLException {

        String req = "INSERT INTO `user`(`firstname`, `lastname`, `age`) " +
                "VALUES ('"+user.getFirstName()+"','"+user.getLastName()+"', "+user.getAge()+")";

        st = cnx.createStatement();

        return st.executeUpdate(req);
    }

    public int insert1(User user) throws SQLException {

        String req = "INSERT INTO `user`(`firstname`, `lastname`, `age`) VALUES (?, ?, ?)";

        ps = cnx.prepareStatement(req);

        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());

        return ps.executeUpdate();
    }

    @Override
    public int update(User user) throws SQLException {
        String req = "UPDATE `user` SET `firstname` = ?, `lastname` = ?, `age` = ? WHERE `firstname` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());
        return ps.executeUpdate();
    }

    @Override
    public int delete(User user) throws SQLException {
        String req = "DELETE FROM `user` WHERE `fistname` = ?";
        ps = cnx.prepareStatement(req);
        ps.setString(1, user.getFirstName()); ;
        return ps.executeUpdate();
    }

    @Override
    public List<User> showAll() throws SQLException{
        List<User> temp = new ArrayList<>();

        String req = "SELECT * FROM `user`";

        st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            User s = new User();
            s.setId(rs.getInt(1));
            s.setFirstName(rs.getString("firstname"));
            s.setLastName(rs.getString(3));
            s.setAge(rs.getInt("age"));

            temp.add(s);
        }

        return temp;
    }
}

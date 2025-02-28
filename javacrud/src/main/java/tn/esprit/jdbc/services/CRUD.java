package tn.esprit.jdbc.services;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {
<<<<<<< HEAD
    int insert(T t) throws SQLException;
    int update(T t) throws SQLException;
    int delete(T t) throws SQLException;

    int delete(int userId) throws SQLException;
    //int delete(int userid) throws SQLException;

    List<T> showAll() throws SQLException;
}
=======
    int insert(T t) throws SQLException; // Insert a new record
    int update(T t) throws SQLException; // Update an existing record
    int delete(int id) throws SQLException; // Delete a record by its ID
    List<T> showAll() throws SQLException; // Retrieve all records
}
>>>>>>> 0c71dfb834f6add887b2b67a7725ae848c89067d

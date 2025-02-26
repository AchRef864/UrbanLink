package tn.esprit.jdbc.services;

import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {
    int insert(T t) throws SQLException; // Insert a new record
    int update(T t) throws SQLException; // Update an existing record
    int delete(int id) throws SQLException; // Delete a record by its ID
    List<T> showAll() throws SQLException; // Retrieve all records
}
package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Person;
import tn.esprit.jdbc.services.PersonService;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.SQLException;

public class MainTest {

    public static void main(String[] args) {
        MyDatabase m1 = MyDatabase.getInstance();

        Person p1 = new Person("Hbib","Khamouma",24);
        Person p2 = new Person("Amine","Jery",21);

        PersonService personService = new PersonService();

        try {
            personService.insert(p1);
            personService.insert1(p2);

            System.out.println(personService.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}

package tn.esprit.services;

import tn.esprit.utils.MyDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    /**
     * Vérifie si l'utilisateur dont l'ID est userId est un administrateur.
     * Dans la table 'users', la colonne 'admin' vaut 1 pour un admin, 0 sinon.
     */
    public boolean isAdmin(int userId) {
        boolean isAdmin = false;
        Connection cnx = MyDataBase.getInstance().getConnection();
        // Utilisation de 'user_id' puisque c'est le nom de la colonne dans la table
        String req = "SELECT admin FROM users WHERE user_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int adminValue = rs.getInt("admin");
                    System.out.println("Pour l'utilisateur " + userId + ", valeur admin = " + adminValue);
                    isAdmin = (adminValue == 1);
                } else {
                    System.out.println("Aucun utilisateur trouvé avec l'id " + userId);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return isAdmin;
    }

    /**
     * Vérifie si l'utilisateur est un administrateur sans se baser sur l'ID.
     * Exemple : Cela pourrait être un utilisateur par défaut ou un rôle par défaut.
     */
    public boolean isUsersengeneral(int userId) {
        boolean isUsersengeneral = false;
        Connection cnx = MyDataBase.getInstance().getConnection();
        String req = "SELECT user_id FROM users WHERE user_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isUsersengeneral = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

    }
        return isUsersengeneral;
    }


}

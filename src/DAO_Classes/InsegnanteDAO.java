package DAO_Classes;

import Entity_Classes.Insegnante;
import exception.LoginException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InsegnanteDAO {

    public static Insegnante login(String username, String password) throws LoginException {
        String sql = "SELECT * FROM insegnanti WHERE username = ? AND password = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate dataNascita = rs.getDate("data_nascita").toLocalDate();

                    return new Insegnante(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("strumento"),
                        dataNascita
                    );
                } else {
                    throw new LoginException("Credenziali non valide.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new LoginException("Errore di accesso al database.");
        }
    }
}

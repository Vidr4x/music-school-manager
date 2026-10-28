package DAO_Classes;

import Entity_Classes.Studente;
import exception.LoginException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudenteDAO {

    public static Studente login(String username, String password) throws LoginException {
        String sql = "SELECT * FROM studente WHERE username = ? AND password = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Studente studente = new Studente();
                    studente.setId(rs.getInt("id"));
                    studente.setNome(rs.getString("nome"));
                    studente.setCognome(rs.getString("cognome"));
                    studente.setDataNascita(rs.getDate("dataNascita"));
                    studente.setTelefono(rs.getString("telefono"));
                    studente.setEmail(rs.getString("email"));
                    studente.setUsername(rs.getString("username"));
                    studente.setPassword(rs.getString("password"));
                    return studente;
                } else {
                    throw new LoginException("Credenziali non valide.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new LoginException("Errore durante l'accesso al database.");
        }
    }

    public List<Studente> readStudenti(String nome, String cognome) throws SQLException {
        List<Studente> studenti = new ArrayList<>();
        String query = "SELECT * FROM studente WHERE nome = ? AND cognome = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Studente studente = new Studente();
                    studente.setId(rs.getInt("id"));
                    studente.setNome(rs.getString("nome"));
                    studente.setCognome(rs.getString("cognome"));
                    studente.setDataNascita(rs.getDate("dataNascita"));
                    studente.setTelefono(rs.getString("telefono"));
                    studente.setEmail(rs.getString("email"));
                    studente.setUsername(rs.getString("username"));
                    studente.setPassword(rs.getString("password"));
                    studenti.add(studente);
                }
            }
        }

        return studenti;
    }

    public List<Studente> readAllStudenti() throws SQLException {
        List<Studente> studenti = new ArrayList<>();
        String query = "SELECT * FROM studente";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Studente studente = new Studente();
                studente.setId(rs.getInt("id"));
                studente.setNome(rs.getString("nome"));
                studente.setCognome(rs.getString("cognome"));
                studente.setDataNascita(rs.getDate("dataNascita"));
                studente.setTelefono(rs.getString("telefono"));
                studente.setEmail(rs.getString("email"));
                studente.setUsername(rs.getString("username"));
                studente.setPassword(rs.getString("password"));
                studenti.add(studente);
            }
        }

        return studenti;
    }
}

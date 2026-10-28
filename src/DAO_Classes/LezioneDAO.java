package DAO_Classes;

import Entity_Classes.Lezione;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LezioneDAO {
    private final String DB_URL = "jdbc:h2:~/SistemiLezioneMusica";
    private final String USER = "sa";
    private final String PASSWORD = "";

    public List<Lezione> getLezioniDisponibili(int id, String insegnante, double prezzo, String strumento) {
        List<Lezione> lezioni = new ArrayList<>();
        String sql = "SELECT * FROM DISPONIBILITA WHERE ID = ? AND INSEGNANTE = ? AND PREZZO = ? " +
                     "AND LIVELLO IN ('principianti', 'intermedio') AND STRUMENTO = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, insegnante);
            stmt.setDouble(3, prezzo);
            stmt.setString(4, strumento);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lezioni.add(mapRowToLezione(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lezioni;
    }

    public List<Lezione> getLezioniDisponibili() throws SQLException {
        List<Lezione> lezioni = new ArrayList<>();
        String sql = "SELECT * FROM DISPONIBILITA WHERE STATO = 'disponibile'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lezioni.add(mapRowToLezione(rs));
            }
        }

        return lezioni;
    }

    public List<Lezione> getLezioniFiltrate(String livello, double prezzoMax, String strumento) {
        List<Lezione> lezioni = new ArrayList<>();
        String sql = "SELECT * FROM DISPONIBILITA WHERE STATO = 'disponibile' " +
                     "AND UPPER(LIVELLO) = ? AND PREZZO <= ? AND UPPER(STRUMENTO) = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livello.toUpperCase());
            stmt.setDouble(2, prezzoMax);
            stmt.setString(3, strumento.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lezioni.add(mapRowToLezione(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lezioni;
    }

    public List<Lezione> getLezioniDisponibiliConStato(int id, double prezzo, String strumento) {
        List<Lezione> lezioni = new ArrayList<>();
        String sql = "SELECT * FROM DISPONIBILITA WHERE ID = ? AND PREZZO = ? " +
                     "AND (LIVELLO = 'principianti' OR LIVELLO = 'intermedio') " +
                     "AND STRUMENTO = ? AND STATO = 'disponibile'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setDouble(2, prezzo);
            stmt.setString(3, strumento);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lezioni.add(mapRowToLezione(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lezioni;
    }

    public boolean controllaDisponibilita(int id) {
        String sql = "SELECT COUNT(*) FROM DISPONIBILITA WHERE ID = ? AND STATO = 'disponibile'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void prenotaLezione(int idLezione, String livello, double prezzo, String strumento) {
        String sql = "UPDATE DISPONIBILITA SET STATO = 'prenotata' WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLezione);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void impostaStatoPrenotata(int idLezione) {
        String sql = "UPDATE DISPONIBILITA SET STATO = 'prenotata' WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLezione);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per costruire oggetto Lezione includendo data e ora
    private Lezione mapRowToLezione(ResultSet rs) throws SQLException {
        return new Lezione(
                rs.getInt("ID"),
                rs.getString("Livello"),
                rs.getDouble("Prezzo"),
                rs.getString("Insegnante"),
                rs.getString("Stato"),
                rs.getString("Strumento"),
                rs.getString("Data"),
                rs.getString("Ora")
        );
    }
}

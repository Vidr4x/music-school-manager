package DAO_Classes;

import Entity_Classes.Disponibilita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisponibilitaDAO {

    public static List<Disponibilita> getDisponibilitaByInsegnante(int idInsegnante) {
        List<Disponibilita> lista = new ArrayList<>();

        String sql = "SELECT * FROM disponibilita WHERE id_insegnante = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInsegnante);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Disponibilita(
                            rs.getInt("id"),
                            rs.getInt("id_insegnante"),
                            rs.getString("data"),
                            rs.getString("ora"),
                            rs.getString("strumento"),
                            rs.getString("livello"),
                            rs.getDouble("prezzo"),
                            rs.getString("stato"),
                            rs.getString("insegnante")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle disponibilità: " + e.getMessage());
        }

        return lista;
    }

    public static void aggiornaDisponibilita(Disponibilita d) {
        String sql = "UPDATE disponibilita SET data = ?, ora = ?, strumento = ?, livello = ?, prezzo = ?, stato = ?, insegnante = ? WHERE id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, d.getData());
            stmt.setString(2, d.getOra());
            stmt.setString(3, d.getStrumento());
            stmt.setString(4, d.getLivello());
            stmt.setDouble(5, d.getPrezzo());
            stmt.setString(6, d.getStato());
            stmt.setString(7, d.getInsegnante());
            stmt.setInt(8, d.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della disponibilità: " + e.getMessage());
        }
    }

    public static void aggiungiDisponibilita(Disponibilita d) {
        String sql = "INSERT INTO disponibilita " +
                "(id, id_insegnante, data, ora, strumento, livello, prezzo, stato, insegnante) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int nuovoId = getProssimoIdDisponibilita(); // genera un nuovo ID valido

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuovoId);
            stmt.setInt(2, d.getIdDocente());
            stmt.setString(3, d.getData());
            stmt.setString(4, d.getOra());
            stmt.setString(5, d.getStrumento());
            stmt.setString(6, d.getLivello());
            stmt.setDouble(7, d.getPrezzo());
            stmt.setString(8, d.getStato());
            stmt.setString(9, d.getInsegnante());

            stmt.executeUpdate();
            System.out.println("Disponibilità inserita nel database con ID: " + nuovoId);

        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento della disponibilità: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int getProssimoIdDisponibilita() {
        String sql = "SELECT MAX(ID) FROM DISPONIBILITA";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il calcolo del prossimo ID: " + e.getMessage());
        }
        return 1; // default se la tabella è vuota
    }
    public static List<Disponibilita> getAllDisponibilita() {
        List<Disponibilita> lista = new ArrayList<>();

        String sql = "SELECT * FROM disponibilita";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Disponibilita(
                        rs.getInt("id"),
                        rs.getInt("id_insegnante"),
                        rs.getString("data"),
                        rs.getString("ora"),
                        rs.getString("strumento"),
                        rs.getString("livello"),
                        rs.getDouble("prezzo"),
                        rs.getString("stato"),
                        rs.getString("insegnante")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero di tutte le disponibilità: " + e.getMessage());
        }

        return lista;
    }

}
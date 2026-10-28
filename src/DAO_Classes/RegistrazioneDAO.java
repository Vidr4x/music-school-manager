package DAO_Classes;

import Entity_Classes.Registrazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrazioneDAO {

    public Registrazione readRegistrazione(int studenteId) throws SQLException {
        String query = "SELECT * FROM Registrazioni WHERE studente_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, studenteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Registrazione registrazione = new Registrazione();
                    registrazione.setStudenteId(rs.getInt("studente_id"));
                    registrazione.setValidita(rs.getBoolean("validita"));
                    return registrazione;
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nel recupero della registrazione: " + e.getMessage());
            throw e;
        }

        return null; // Nessuna registrazione trovata
    }

    public boolean updateRegistrazione(int studenteId, int pagamentoId) throws SQLException {
        String query = "UPDATE Registrazioni SET validita = TRUE WHERE studente_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, studenteId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento della registrazione: " + e.getMessage());
            throw e;
        }
    }
}

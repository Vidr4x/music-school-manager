package DAO_Classes;

import Entity_Classes.Pagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PagamentoDAO {

    public Pagamento readPagamento(int studenteId, Date meseRiferimento) throws SQLException {
        String query = "SELECT * FROM Pagamenti WHERE studente_id = ? AND mese_riferimento = ? " +
                       "ORDER BY data_pagamento DESC LIMIT 1";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, studenteId);
            pstmt.setDate(2, new java.sql.Date(meseRiferimento.getTime()));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setId(rs.getInt("id"));
                    pagamento.setStudenteId(rs.getInt("studente_id"));
                    pagamento.setImporto(rs.getDouble("importo"));
                    pagamento.setMeseRiferimento(rs.getDate("mese_riferimento"));
                    pagamento.setDataPagamento(rs.getDate("data_pagamento"));
                    return pagamento;
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nel recupero del pagamento: " + e.getMessage());
            throw e;
        }

        return null; // Nessun pagamento trovato
    }
}

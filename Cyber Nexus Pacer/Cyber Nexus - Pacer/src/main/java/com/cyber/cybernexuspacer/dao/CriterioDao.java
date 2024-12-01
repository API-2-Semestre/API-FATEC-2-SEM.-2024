package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.Criterio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriterioDao {
    public void inserirCriterio(Criterio criterio) {
        String sql = "INSERT INTO CRITERIOS (TITULO, DESCRICAO) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, criterio.getTitulo());
            stmt.setString(2, criterio.getDescricao());
            stmt.executeUpdate();
            System.out.println("Critério inserido com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Criterio> listarCriterios() throws SQLException {
        List<Criterio> criterios = new ArrayList<>();
        String sql = "SELECT * FROM CRITERIOS";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connection connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String descricao = rs.getString("descricao");

                Criterio criterio = new Criterio( titulo, descricao);
                criterios.add(criterio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return criterios;
    }

    // Método para deletar critério pelo ID
    public void deletarCriterio(String titulo) throws SQLException {
        String sql = "DELETE FROM CRITERIOS WHERE TITULO = ?";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}

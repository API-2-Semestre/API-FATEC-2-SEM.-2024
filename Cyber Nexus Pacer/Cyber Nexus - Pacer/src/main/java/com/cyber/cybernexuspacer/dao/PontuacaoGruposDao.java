package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.PontuacaoGrupo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PontuacaoGruposDao {

    public static List<PontuacaoGrupo> pesquisarGrupos() throws SQLException {
        String sql = """
                SELECT G.ID, G.GRUPO, NG.NOTA_GRUPO, COUNT(A.id) AS INTEGRANTES
                FROM GRUPOS AS G
                LEFT JOIN NOTAS_GRUPOS AS NG ON G.ID = NG.ID
                LEFT JOIN ALUNOS AS A ON G.GRUPO = A.GRUPO
                GROUP BY G.ID, G.GRUPO,NG.NOTA_GRUPO;
                """;

        List<PontuacaoGrupo> pontGrupos = new ArrayList<>();
        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itera sobre o ResultSet para preencher a lista de grupos
            while (rs.next()) {
                int id = rs.getInt("id");
                String grupo = rs.getString("grupo");
                int integrantes = rs.getInt("integrantes");
                int nota = rs.getInt("nota_grupo");

                // Adiciona o grupo à lista
                pontGrupos.add(new PontuacaoGrupo(id, grupo, integrantes, nota));
            }
        }
        return pontGrupos;
    }

    public static void salvarNotaGrupo(String nomeGrupo, int numSprint, int novaNota) throws SQLException {
        String sql = "UPDATE NOTAS_GRUPOS SET NOTA_GRUPO = ? WHERE GRUPO = ? AND NUM_SPRINT = ?";

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, novaNota);
            stmt.setString(2, nomeGrupo);
            stmt.setInt(3, numSprint);

            stmt.executeUpdate();
        }
    }


    public static List<Integer> selecionarNotasSprint(String grupo, int numSprint) throws SQLException {
        String sql = "SELECT NOTA_GRUPO FROM NOTAS_GRUPOS WHERE GRUPO = ? AND NUM_SPRINT = ?";

        List<Integer> notas = new ArrayList<>();
        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, grupo);
            stmt.setInt(2, numSprint);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notas.add(rs.getInt("NOTA_GRUPO"));
                }
            }
        }
        return notas;
    }

    public static void liberarSprint(int numSprint) throws SQLException {
        String sql = """
            UPDATE SPRINTS
            SET LIBERADO = 1, DATA_LIBERACAO = CURRENT_DATE
            WHERE NUM_SPRINT = ?;
            """;

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numSprint); // Configura o número da sprint

            stmt.executeUpdate(); // Executa o update no banco
        }
    }

    public static void bloquearSprint(int numSprint) throws SQLException {
        String sql = """
            UPDATE SPRINTS
            SET LIBERADO = 0
            WHERE NUM_SPRINT = ?;
            """;

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, numSprint); // Configura o número da sprint

            stmt.executeUpdate(); // Executa o update no banco
        }
    }

}

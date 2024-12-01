package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.Sprint;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SprintDao {

    // Salva um novo sprint no banco de dados


    public int salvarSprint(Sprint sprint) {
        String fetchSprintsSql = "SELECT NUM_SPRINT, DATA_FINAL FROM SPRINTS ORDER BY NUM_SPRINT ASC"; // Consulta para buscar sprints existentes
        String insertSql = "INSERT INTO SPRINTS (NUM_SPRINT, DATA_INICIAL, DATA_FINAL, DIAS_LIBERADOS, DATA_LIBERACAO, LIBERADO) VALUES (?, ?, ?,NULL,NULL,0)";

        try (Connection connection = ConexaoDao.getConnection()) {
            // 1. Buscar todas as sprints existentes
            List<Integer> sprintsExistentes = new ArrayList<>();
            Date ultimaDataFinal = null; // Para armazenar a última data final

            try (PreparedStatement fetchStmt = connection.prepareStatement(fetchSprintsSql);
                 ResultSet resultSet = fetchStmt.executeQuery()) {
                while (resultSet.next()) {
                    sprintsExistentes.add(resultSet.getInt("NUM_SPRINT"));
                    ultimaDataFinal = resultSet.getDate("DATA_FINAL"); // Atualiza com a última data final encontrada
                }
            }

            // 2. Determinar o próximo número de sprint
            int proximaSprint = 1; // Caso nenhuma sprint exista, comece com "1"
            for (int i = 1; i <= sprintsExistentes.size() + 1; i++) {
                if (!sprintsExistentes.contains(i)) {
                    proximaSprint = i;
                    break; // Encontre o primeiro número ausente e saia do loop
                }
            }

            // 3. Validar a data inicial da nova sprint
            if (ultimaDataFinal != null && !sprint.getDataInicio().after(ultimaDataFinal)) {
                exibirMensagemPopup("A data inicial da nova sprint deve ser posterior à data final da última sprint.");
                return 0; // Interrompe a operação se a validação falhar
            }

            // 4. Inserir a nova sprint
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                if (sprint.getDataInicio() == null || sprint.getDataFim() == null) {
                    exibirMensagemPopup("Datas não podem ser vazias.");
                    return 0; // Interrompe a operação se as datas forem inválidas
                }

                insertStmt.setInt(1, proximaSprint); // Configura o número correto da sprint
                insertStmt.setDate(2, sprint.getDataInicio());
                insertStmt.setDate(3, sprint.getDataFim());
                insertStmt.executeUpdate();

                // 5. Obter o ID gerado
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retorna o ID gerado
                    } else {
                        throw new SQLException("Falha ao obter o ID gerado.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void exibirMensagemPopup(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validação de Data");
        alert.setHeaderText("Erro de Validação");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Lista todos os sprints no banco de dados
    public List<Sprint> listarSprints() throws SQLException {
        List<Sprint> sprints = new ArrayList<>();
        String sql = "SELECT * FROM SPRINTS";

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int numSprint = rs.getInt("num_sprint");
                Date dataInicial = rs.getDate("data_inicial");
                Date dataFinal = rs.getDate("data_final");

                Sprint sprint = new Sprint(id, numSprint, dataInicial, dataFinal);
                sprint.setLiberado(rs.getInt("liberado"));
                sprints.add(sprint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sprints;
    }

    // Deleta um sprint específico pelo ID
    public void deletarSprint(int id) throws SQLException {
        String sql = "DELETE FROM SPRINTS WHERE id = ?";

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar o sprint", e);
        }
    }

    // Obtém o sprint atual com base na data
    public static Sprint obterSprintAtual() throws SQLException {
        //String sql = "SELECT * FROM SPRINTS WHERE CURDATE() BETWEEN DATA_INICIAL AND DATA_FINAL LIMIT 1";
        String sql = "SELECT * FROM SPRINTS WHERE CURDATE() BETWEEN DATA_INICIAL AND DATA_FINAL LIMIT 1";

        Sprint sprintAtual = null;

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                sprintAtual = new Sprint();
                sprintAtual.setId(rs.getInt("id"));
                sprintAtual.setNumSprint(rs.getInt("num_sprint"));
                sprintAtual.setDataInicio(rs.getDate("data_inicial"));
                sprintAtual.setDataFim(rs.getDate("data_final"));
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar o sprint atual", e);
        }

        return sprintAtual;
    }


    public Date buscarDataFinalPrimeiraSprint() throws SQLException {
        String sql = "SELECT DATA_FINAL FROM SPRINTS ORDER BY NUM_SPRINT ASC LIMIT 1";

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDate("DATA_FINAL");
            }
        }
        return null; // Retorna null se nenhuma sprint for encontrada
    }

    public static void atualizarDiasLiberados(int numSprint, int diasLiberados) throws SQLException {
        String sql = """
            UPDATE SPRINTS
            SET DIAS_LIBERADOS = ?
            WHERE NUM_SPRINT = ?;
            """;

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, diasLiberados); // Configura o valor de dias liberados
            stmt.setInt(2, numSprint);     // Configura o número da sprint

            stmt.executeUpdate(); // Executa o update no banco
        }
    }


}

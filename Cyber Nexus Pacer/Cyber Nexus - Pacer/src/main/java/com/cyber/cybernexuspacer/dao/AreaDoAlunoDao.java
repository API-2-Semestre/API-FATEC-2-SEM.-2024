package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.AreaDoAluno;
import com.cyber.cybernexuspacer.entity.Sprint;
import com.cyber.cybernexuspacer.session.AlunoSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AreaDoAlunoDao {

    // Método para listar os alunos do mesmo grupo que o aluno logado
    public List<AreaDoAluno> listarAlunos() throws SQLException {
        List<AreaDoAluno> alunos = new ArrayList<>();

        // Verifica se o aluno está logado
        AreaDoAluno alunoLogado = AlunoSession.getAlunoLogado();
        if (alunoLogado == null) {
            throw new SQLException("Nenhum aluno logado.");
        }

        // Obtém o e-mail do aluno logado
        String emailAlunoLogado = alunoLogado.getEmail();

        // Consulta SQL para obter o id_grupo do aluno
        String sqlGrupo = "SELECT GRUPO FROM ALUNOS WHERE email = ?";
        String sqlAlunosDoGrupo = "SELECT ID, NOME, EMAIL, GRUPO FROM ALUNOS WHERE GRUPO = ?";
        String sqlAlunoLogado = "SELECT ID FROM ALUNOS WHERE email = ?";


        PreparedStatement stmtGrupo = null;
        PreparedStatement stmtAlunosDoGrupo = null;
        PreparedStatement stmtAlunoLogado = null;
        ResultSet rsGrupo = null;
        ResultSet rsAlunosDoGrupo = null;
        ResultSet rsAlunoLogado = null;

        try {
            // Conexão com o banco de dados
            Connection connection = ConexaoDao.getConnection();
            assert connection != null;

            // Obtém o id_grupo do aluno logado
            stmtGrupo = connection.prepareStatement(sqlGrupo);
            stmtGrupo.setString(1, emailAlunoLogado);
            rsGrupo = stmtGrupo.executeQuery();

            stmtAlunoLogado = connection.prepareStatement(sqlAlunoLogado);
            stmtAlunoLogado.setString(1, emailAlunoLogado);
            rsAlunoLogado = stmtAlunoLogado.executeQuery();


            if (rsGrupo.next()) {
                String grupo = rsGrupo.getString("grupo");

                // Consulta para buscar os alunos do mesmo grupo
                stmtAlunosDoGrupo = connection.prepareStatement(sqlAlunosDoGrupo);
                stmtAlunosDoGrupo.setString(1, grupo);
                rsAlunosDoGrupo = stmtAlunosDoGrupo.executeQuery();


                // Itera sobre o ResultSet e preenche a lista de alunos
                while (rsAlunosDoGrupo.next()) {
                    int idAluno = rsAlunosDoGrupo.getInt("id");
                    String nome = rsAlunosDoGrupo.getString("nome");
                    String email = rsAlunosDoGrupo.getString("email");

                    // Cria e adiciona o aluno à lista
                    AreaDoAluno aluno = new AreaDoAluno(idAluno,nome, email, grupo, "fatec2024", "Aluno");


                    alunos.add(aluno);
                }
                if (rsAlunoLogado.next()) {
                    alunoLogado.setIdAlunoAvaliador(rsAlunoLogado.getInt("ID"));
                } else {
                    // Caso não tenha encontrado o aluno logado, trata-se de um erro
                    throw new SQLException("Aluno logado não encontrado.");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Lança a exceção para ser tratada em um nível superior
        } finally {
            // Fechamento de recursos
            if (rsGrupo != null) rsGrupo.close();
            if (rsAlunosDoGrupo != null) rsAlunosDoGrupo.close();
            if (stmtGrupo != null) stmtGrupo.close();
            if (stmtAlunosDoGrupo != null) stmtAlunosDoGrupo.close();
        }

        return alunos;  // Retorna a lista de alunos
    }

    public double buscaNota(int sprintSelecionada) throws SQLException {
        // Pegando sprint atual
        /*Sprint sprintAtual = SprintDao.obterSprintAtual();

        if (sprintAtual == null) {
            throw new SQLException("Nenhuma sprint ativa encontrada.");
        }*/

        String sql = "SELECT SUM(NOTA_GRUPO) - ( " +
                "    SELECT COALESCE(SUM(NOTA), 0) " +
                "    FROM NOTAS " +
                "    WHERE ID_AVALIADOR IN ( " +
                "        SELECT ID " +
                "        FROM ALUNOS " +
                "        WHERE GRUPO = ? ) " +  // Nome do grupo
                "    AND NUM_SPRINT = ? " +  // Número da sprint
                ") AS resultado " +
                "FROM NOTAS_GRUPOS " +
                "WHERE GRUPO IN ( " +
                "    SELECT GRUPO " +
                "    FROM ALUNOS " +
                "    WHERE ID = ? );";  // ID do avaliador do grupo

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connection connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);


            stmt.setString(1, AlunoSession.getAlunoLogado().getGrupo());
            stmt.setInt(2, sprintSelecionada);
            stmt.setInt(3, AlunoSession.getAlunoLogado().getIdAlunoAvaliador());
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("resultado"); // Retorna a nota do grupo
            } else {
                return 0.0;
            }

        } catch (SQLException e) {
            // Trate a exceção de forma adequada (log, rethrow, etc)
            throw new RuntimeException("Erro ao buscar a nota: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close(); // Fechar o ResultSet
            if (stmt != null) stmt.close(); // Fechar o PreparedStatement
        }
    }

    public void salvarNota(int idAvaliador, int idReceptor, int nota, String tituloCriterio, int sprintSelecionada) throws SQLException {
        // Pegando sprint atual
        /*Sprint sprintAtual = SprintDao.obterSprintAtual();

        if (sprintAtual == null) {
            throw new SQLException("Nenhuma sprint ativa encontrada.");
        }*/

        String sql = "INSERT INTO NOTAS (id_avaliador, id_receptor, nota, titulo_criterio, num_sprint) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Definir os parâmetros do PreparedStatement
            stmt.setInt(1, idAvaliador);          // ID do avaliador
            stmt.setInt(2, idReceptor);           // ID do avaliado
            stmt.setInt(3, nota);                 // Nota
            stmt.setString(4, tituloCriterio);    // Título do critério
            stmt.setInt(5, sprintSelecionada);            // Número do sprint

            // Executar o insert
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Tratar exceções
            System.err.println("Erro ao salvar a nota: " + e.getMessage());
            throw e; // Re-lançar exceção para propagar o erro
        }
    }

}


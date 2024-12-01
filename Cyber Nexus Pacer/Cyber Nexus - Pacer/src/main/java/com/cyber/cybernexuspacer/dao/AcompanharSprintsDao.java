package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.AcompanharSprints;
import com.cyber.cybernexuspacer.entity.AreaDoAluno;
import com.cyber.cybernexuspacer.entity.GrupoSprint;
import com.cyber.cybernexuspacer.entity.PontuacaoGrupo;
import com.cyber.cybernexuspacer.session.AlunoSession;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcompanharSprintsDao {
    public List<PontuacaoGrupo> listarGrupos() throws SQLException {
        List<PontuacaoGrupo> grupos = new ArrayList<>();
        String sql = "SELECT * FROM grupos";  // Tabela de grupos
        try  {
            Connection connection = ConexaoDao.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                PontuacaoGrupo grupo = new PontuacaoGrupo();
                grupo.setGrupo(rs.getString("grupo"));
                grupos.add(grupo);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return grupos;
    }

    // Método para listar os alunos do mesmo grupo
    public List<AreaDoAluno> listarAlunosPorGrupo(String grupo) throws SQLException {
        List<AreaDoAluno> alunos = new ArrayList<>();

        String sqlAlunosDoGrupo = "SELECT ID, NOME, EMAIL, GRUPO FROM ALUNOS WHERE GRUPO = ?";


        PreparedStatement stmtAlunosDoGrupo = null;
        ResultSet rsAlunosDoGrupo = null;

        try {
            // Conexão com o banco de dados
            Connection connection = ConexaoDao.getConnection();
            assert connection != null;


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



        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Lança a exceção para ser tratada em um nível superior
        } finally {
            // Fechamento de recursos
            if (rsAlunosDoGrupo != null) rsAlunosDoGrupo.close();
            if (stmtAlunosDoGrupo != null) stmtAlunosDoGrupo.close();
        }

        return alunos;  // Retorna a lista de alunos
    }

    //Listar todos os alunos
    public List<AreaDoAluno> listarAlunos() throws SQLException {
        List<AreaDoAluno> alunos = new ArrayList<>();

        String  sql = "SELECT ID, NOME, EMAIL, GRUPO FROM ALUNOS";


        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Conexão com o banco de dados
            Connection connection = ConexaoDao.getConnection();
            assert connection != null;


            // Consulta para buscar os alunos do mesmo grupo
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();


            // Itera sobre o ResultSet e preenche a lista de alunos
            while (rs.next()) {
                int idAluno = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String grupo = rs.getString("grupo");

                // Cria e adiciona o aluno à lista
                AreaDoAluno aluno = new AreaDoAluno(idAluno,nome, email, grupo, "fatec2024", "Aluno");

                alunos.add(aluno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Lança a exceção para ser tratada em um nível superior
        } finally {
            // Fechamento de recursos
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return alunos;  // Retorna a lista de alunos
    }

    // Método para pesquisar os grupos e calcular a soma e a média das notas
    public List<AcompanharSprints> listarNotasPorCriterioESprint(String tituloCriterio, long numSprint) throws SQLException {
        List<AcompanharSprints> notas = new ArrayList<>();
        String sql = """
            WITH grupos_com_integrantes AS (
                SELECT
                    a.grupo,
                    COUNT(*) AS num_integrantes
                FROM
                    alunos a
                GROUP BY
                    a.grupo
            ),
            notas_por_grupo_criterio AS (
                SELECT
                    a.grupo,
                    n.titulo_criterio,
                    n.num_sprint,
                    SUM(n.nota) AS soma_notas_grupo
                FROM
                    notas n
                INNER JOIN alunos a ON n.id_avaliador = a.id
                GROUP BY
                    a.grupo, n.titulo_criterio, n.num_sprint
            )
            SELECT
                a.nome AS nome_aluno,
                a.grupo AS grupo_aluno,
                SUM(n.nota) AS soma_notas_aluno,
                ng.soma_notas_grupo,
                (SUM(n.nota) / gci.num_integrantes) AS media_aluno
            FROM
                notas n
            INNER JOIN alunos a ON n.id_receptor = a.id
            INNER JOIN notas_por_grupo_criterio ng ON
                a.grupo = ng.grupo AND
                n.titulo_criterio = ng.titulo_criterio AND
                n.num_sprint = ng.num_sprint
            INNER JOIN grupos_com_integrantes gci ON a.grupo = gci.grupo
            WHERE
                n.titulo_criterio = ?
                AND n.num_sprint = ?
            GROUP BY
                a.nome, a.grupo, ng.soma_notas_grupo, gci.num_integrantes
            ORDER BY
                a.grupo, a.nome;
            """;
        try  {

            Connection conn = ConexaoDao.getConnection();
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tituloCriterio);
            stmt.setLong(2, numSprint);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("nome_aluno");
                String grupo = rs.getString("grupo_aluno");
                double somaGrupo = rs.getDouble("soma_notas_grupo");
                double mediaNota = rs.getDouble("media_aluno");
                //double somaAluno = rs.getDouble("soma_notas_aluno");
                notas.add(new AcompanharSprints(nome, grupo, mediaNota, somaGrupo));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return notas;
    }

    public void atualizarGrupoAluno(long idAluno, String nomeAluno, String novoGrupo) throws SQLException {
        // Primeiro, obter o grupo atual do aluno
        String grupoAtual = obterGrupoAtualAluno(idAluno);

        // Registrar o histórico
        salvarHistoricoGrupo(idAluno, grupoAtual, novoGrupo);

        // Atualizar o grupo no banco
        String sql = "UPDATE alunos SET grupo = ? WHERE id = ?";
        try {
            Connection conn = ConexaoDao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoGrupo);
            stmt.setLong(2, idAluno);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String obterGrupoAtualAluno(long idAluno) throws SQLException {
        String sql = "SELECT grupo FROM alunos WHERE id = ?";
        try {
            Connection conn = ConexaoDao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("grupo");
            } else {
                throw new SQLException("Aluno não encontrado com id: " + idAluno);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long obterIdPorNome(String nomeAluno) throws SQLException {
        String sql = "SELECT id FROM alunos WHERE nome = ?";
        try {
            Connection conn = ConexaoDao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeAluno);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                } else {
                    throw new SQLException("Aluno não encontrado com o nome: " + nomeAluno);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void salvarHistoricoGrupo(long idAluno, String grupoAnterior, String grupoAtual) throws SQLException {
        String sql = "INSERT INTO historico_grupo_aluno (id_aluno, grupo_anterior, grupo_atual) VALUES (?, ?, ?)";
        try {
            Connection conn = ConexaoDao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idAluno);
            stmt.setString(2, grupoAnterior);
            stmt.setString(3, grupoAtual);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<GrupoSprint> listarDadosParaRelatorio() throws SQLException {
        List<GrupoSprint> dados = new ArrayList<>();
        String sql = "SELECT grupo, num_sprint, nota_grupo FROM notas_grupos ORDER BY grupo, num_sprint";

        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            Map<String, GrupoSprint> gruposMap = new HashMap<>();

            while (resultSet.next()) {
                String nomeGrupo = resultSet.getString("grupo");
                int nota = resultSet.getInt("nota_grupo");

                GrupoSprint grupo = gruposMap.computeIfAbsent(nomeGrupo, GrupoSprint::new);
                grupo.adicionarNota(nota);
            }

            dados.addAll(gruposMap.values());
        }
        return dados;
    }

    public List<AcompanharSprints> listarDadosParaRelatorioAlunos() throws SQLException {
        List<AcompanharSprints> alunos = new ArrayList<>();
        String sql = """
                WITH grupos_com_integrantes AS (
                            SELECT
                                a.grupo,
                                COUNT(*) AS num_integrantes
                            FROM alunos a
                            GROUP BY a.grupo)
                        SELECT
                            a.nome,
                            c.titulo,
                            a.grupo,
                            SUM(n.nota) / AVG(gci.num_integrantes) AS media_aluno,
                            n.num_sprint
                        FROM
                            notas n
                        INNER JOIN alunos a ON n.id_receptor = a.id
                        INNER JOIN criterios c ON n.titulo_criterio = c.titulo
                        INNER JOIN grupos_com_integrantes gci ON a.grupo = gci.grupo
                        GROUP BY a.nome, c.titulo, a.grupo, n.num_sprint
                        ORDER BY a.nome;""";

        try  {
            Connection connection = ConexaoDao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nomeAluno = resultSet.getString("nome");
                String grupo = resultSet.getString("grupo");
                double nota = resultSet.getDouble("media_aluno");
                String criterio = resultSet.getString("titulo");
                int sprint = resultSet.getInt("num_sprint");

                AcompanharSprints aluno = new AcompanharSprints();
                aluno.setNomeAluno(nomeAluno);
                aluno.setGrupo(grupo);
                aluno.setMediaNotaAluno(nota);
                aluno.setCriterio(criterio);
                aluno.setSprint(sprint);

                alunos.add(aluno);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return alunos;
    }

    // Método que lista as notas do aluno específico
    public List<AcompanharSprints> listarNotasDoAluno(String nomeAluno, int numSprint) throws SQLException {
        List<AcompanharSprints> notasAluno = new ArrayList<>();

        // SQL que busca as notas do aluno para uma sprint específica
        String sql = "SELECT a.nome AS nome_aluno, g.grupo, s.num_sprint, c.titulo AS criterio, n.nota AS media_nota_aluno " +
                "FROM notas n " +
                "INNER JOIN alunos a ON a.id = n.id_receptor " +
                "INNER JOIN grupos g ON g.grupo = a.grupo " +
                "INNER JOIN sprints s ON s.num_sprint = n.num_sprint " +
                "INNER JOIN criterios c ON c.titulo = n.titulo_criterio " +
                "WHERE a.nome = ? AND s.num_sprint = ? " +
                "ORDER BY c.titulo";

        Connection connection = null;

        try {


            connection = ConexaoDao.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, nomeAluno);
            stmt.setInt(2, numSprint);

            ResultSet rs = stmt.executeQuery();

            // Processar o resultado da consulta
            while (rs.next()) {
                AcompanharSprints sprints = new AcompanharSprints();
                sprints.setNomeAluno(rs.getString("nome_aluno"));
                sprints.setGrupo(rs.getString("grupo"));
                sprints.setSprint(rs.getInt("num_sprint"));
                sprints.setCriterio(rs.getString("criterio"));
                sprints.setMediaNotaAluno(rs.getInt("media_nota_aluno"));

                // Adicionar os dados de notas à lista
                notasAluno.add(sprints);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return notasAluno;
    }


}

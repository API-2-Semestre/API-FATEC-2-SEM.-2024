package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.AreaDoAluno;

import java.sql.*;
import java.util.Calendar;

public class LoginDao {

    public boolean verificarLogin(String nome, String senha, String tipoUsuario) throws SQLException {
       //String sql = "SELECT * FROM SPRINTS WHERE CURDATE() BETWEEN DATA_INICIAL AND DATA_FINAL LIMIT 1";
        String sql = "SELECT * FROM USUARIOS WHERE EMAIL = ? AND SENHA = ? AND TIPO_USUARIO = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connection connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            stmt.setString(3, tipoUsuario);

            rs = stmt.executeQuery();

            return rs.next();  // Retorna verdadeiro se houver correspondência
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    public boolean atualizarSenha(String nome, String novaSenha) throws SQLException {
        String sql = "UPDATE USUARIOS SET SENHA = ? WHERE EMAIL = ?";
        PreparedStatement stmt = null;

        try {
            Connection connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, novaSenha);
            stmt.setString(2, nome);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Retorna verdadeiro se a atualização foi bem-sucedida
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    // Método para buscar os detalhes do aluno por email
    public AreaDoAluno buscarAlunoPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM ALUNOS WHERE EMAIL = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        AreaDoAluno areaDoAluno = null;

        try {
            Connection connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();


            if (rs.next()) {
                int idAluno = rs.getInt("id");
                String nome = rs.getString("nome");
                String nomeGrupo = rs.getString("grupo");


                // Cria o objeto AreaDoAluno e retorna
                areaDoAluno = new AreaDoAluno(idAluno,nome, email, nomeGrupo, "fatec2024", "Aluno");


            } else {
                return null;  // Retorna null se não encontrar o aluno
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return areaDoAluno;
    }

    // Método para verificar o acesso do aluno
    public boolean verificarAcessoAluno(String emailAluno) throws SQLException {
        String sql = """
            SELECT S.DATA_LIBERACAO, S.DIAS_LIBERADOS, NG.NUM_SPRINT
            FROM NOTAS_GRUPOS NG
            JOIN SPRINTS S ON NG.NUM_SPRINT = S.NUM_SPRINT
            WHERE NG.GRUPO = (SELECT GRUPO FROM ALUNOS WHERE EMAIL = ?)
            AND S.LIBERADO = 1;
        """;

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connection connection = ConexaoDao.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, emailAluno);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Obter a data de liberação e os dias liberados
                Date dataLiberacao = rs.getDate("DATA_LIBERACAO");
                int diasLiberados = rs.getInt("DIAS_LIBERADOS");

                // Calcular a data limite (data de liberação + dias liberados)
                Calendar cal = Calendar.getInstance();
                cal.setTime(dataLiberacao);
                cal.add(Calendar.DAY_OF_YEAR, diasLiberados);

                Date dataLimite = new Date(cal.getTimeInMillis());

                // Zerar hora, minuto, segundo e milissegundo das datas
                Calendar calDataAtual = Calendar.getInstance();
                calDataAtual.setTimeInMillis(System.currentTimeMillis());
                calDataAtual.set(Calendar.HOUR_OF_DAY, 0);
                calDataAtual.set(Calendar.MINUTE, 0);
                calDataAtual.set(Calendar.SECOND, 0);
                calDataAtual.set(Calendar.MILLISECOND, 0);
                Date dataAtual = new Date(calDataAtual.getTimeInMillis());

                // Zerar hora, minuto, segundo e milissegundo da data limite
                Calendar calDataLimite = Calendar.getInstance();
                calDataLimite.setTimeInMillis(dataLimite.getTime());
                calDataLimite.set(Calendar.HOUR_OF_DAY, 0);
                calDataLimite.set(Calendar.MINUTE, 0);
                calDataLimite.set(Calendar.SECOND, 0);
                calDataLimite.set(Calendar.MILLISECOND, 0);
                Date dataLimiteSemHora = new Date(calDataLimite.getTimeInMillis());

                return !dataAtual.after(dataLimiteSemHora);  // Se a data atual for antes ou igual à data limite, o acesso é permitido
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return false;  // Retorna false se não encontrar a data de liberação ou se o acesso não for liberado
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection connection = ConexaoDao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

}

package com.cyber.cybernexuspacer.dao;

import com.cyber.cybernexuspacer.entity.AreaDoAluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastroTurmaDao {

    public static void CadastrarAlunos(AreaDoAluno aluno,Connection connection) throws SQLException {

        String sqlUsuario = "INSERT INTO USUARIOS (EMAIL, SENHA, TIPO_USUARIO) VALUES(?,?,?) ";
        String sqlGrupo = "INSERT IGNORE INTO GRUPOS (GRUPO) VALUES(?) "; // MYSQL

        //Caso inserir registros no banco online, utilize o sqlGrupo abaixo, no banco local utilize o sqlGrupo acima
        //String sqlGrupo = "INSERT INTO GRUPOS (GRUPO) VALUES (?) ON CONFLICT (GRUPO) DO NOTHING"; //POSTGRES

        String sqlAluno = "INSERT INTO ALUNOS (NOME,EMAIL, GRUPO) VALUES(?, ?, ?)";
        String sqlNotasGrupos =
                "INSERT INTO notas_grupos (grupo, num_sprint, nota_grupo) " +
                        "SELECT g.grupo, s.num_sprint, 0 " +
                        "FROM grupos g " +
                        "CROSS JOIN sprints s " +
                        "WHERE NOT EXISTS ( " +
                        "SELECT 1 FROM notas_grupos ng " +
                        "WHERE ng.grupo = g.grupo " +
                        "AND ng.num_sprint = s.num_sprint " +
                        ");";


        try (
                PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario);
                PreparedStatement stmtGrupo = connection.prepareStatement(sqlGrupo);
                PreparedStatement stmtAluno = connection.prepareStatement(sqlAluno);
                PreparedStatement stmtNotasGrupos = connection.prepareStatement(sqlNotasGrupos)
        ) {
            // Inserir usuário
            stmtUsuario.setString(1, aluno.getEmail());
            stmtUsuario.setString(2, aluno.getSenha());
            stmtUsuario.setString(3, aluno.getTipo_usuario());
            stmtUsuario.executeUpdate();

            // Inserir grupo
            stmtGrupo.setString(1, aluno.getGrupo());
            stmtGrupo.executeUpdate();

            // Inserir aluno
            stmtAluno.setString(1, aluno.getNomeAluno());
            stmtAluno.setString(2, aluno.getEmail());
            stmtAluno.setString(3, aluno.getGrupo());
            stmtAluno.executeUpdate();

            // Inserir Notas dos Grupos
            stmtNotasGrupos.executeUpdate();

        } catch (SQLException e) {
            throw e; // Repassa a exceção para ser tratada na controller
        }
    }

    public static boolean alunoExists(AreaDoAluno aluno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ALUNOS WHERE NOME = ?";
        try (PreparedStatement stmt = ConexaoDao.getConnection().prepareStatement(sql)) {
            stmt.setString(1, aluno.getNomeAluno());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}

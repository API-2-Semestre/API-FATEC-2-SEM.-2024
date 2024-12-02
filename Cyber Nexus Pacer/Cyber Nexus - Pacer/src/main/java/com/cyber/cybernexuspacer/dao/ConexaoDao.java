package com.cyber.cybernexuspacer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDao {
    //Descomentar linhas abaixo e das configs para acessar banco online e comentar a config do mysql
    private static final String url = "jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres"; // Substitua pelos seus dados do Supabase
    private static final String user = "postgres.avytuuhwmswsbshvwipz"; // Substitua pelo usuário do Supabase
    private static final String password = "CyberNexusFatec"; // Substitua pela senha do Supabase

   /* private static final String url = "jdbc:mysql://localhost:3306/pacerapi?useTimezone=true&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "Einstein_12!";*/

    private static Connection conn;

    public static Connection getConnection() {
        try {
            //Descomentar linhas abaixo e das configs para acessar banco online e comentar a config do mysql
           if (conn == null || conn.isClosed()) {
                  Class.forName("org.postgresql.Driver"); // Atualizado para o driver PostgreSQL
                 conn = DriverManager.getConnection(url, user, password);
            }

           /*if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Atualizado para o driver MySQL
                conn = DriverManager.getConnection(url, user, password);
            }*/

            return conn;

        } catch(SQLException e){
            //e.printStackTrace();
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            return null;
        } catch(ClassNotFoundException e){
            //throw new RuntimeException(e);
            System.err.println("Driver JDBC não encontrado: " + e.getMessage());
            return null;
        }
    }

    public static void testConnection() {
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } else {
            System.out.println("Falha na conexão com o banco de dados.");
        }
    }
}



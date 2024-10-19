package com.cyber.cybernexuspacer.entity;

public class Aluno {

    private final String aluno;
    private final String email;
    private final String grupo;

    public Aluno(String aluno, String email, String grupo) {
        this.aluno = aluno;
        this.email = email;
        this.grupo = grupo;
    }

    public String getAluno() { return aluno; }
    public String getEmail() { return email; }
    public String getGrupo() { return grupo; }
}

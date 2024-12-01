package com.cyber.cybernexuspacer.entity;

public class PontuacaoGrupo {
    int id;
    String grupo;
    int integrantes;
    int nota;

    public PontuacaoGrupo() {}

    public PontuacaoGrupo(int id, String grupo, int integrantes, int nota) {
        this.id = id;
        this.grupo = grupo;
        this.integrantes = integrantes;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }
    public String getGrupo() {
        return grupo;
    }
    public int getIntegrantes() {
        return integrantes;
    }
    public int getNota() {
        return nota;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setIntegrantes(int integrantes) {
        this.integrantes = integrantes;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}

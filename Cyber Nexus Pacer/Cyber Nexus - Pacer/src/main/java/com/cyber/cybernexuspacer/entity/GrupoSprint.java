package com.cyber.cybernexuspacer.entity;

import java.util.ArrayList;
import java.util.List;


public class GrupoSprint {
    private String nome;
    private List<Integer> notas; // Notas por sprint

    public GrupoSprint(String nomeGrupo) {
        this.nome = nomeGrupo;
        this.notas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public List<Integer> getNotas() {
        return notas;
    }

    public void adicionarNota(int nota) {
        this.notas.add(nota);
    }
}

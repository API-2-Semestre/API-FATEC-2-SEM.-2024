package com.cyber.cybernexuspacer.entity;

public class Criterio {
    private int id;
    private String titulo;
    private String descricao;

    // Construtor sem ID
    public Criterio(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }

    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

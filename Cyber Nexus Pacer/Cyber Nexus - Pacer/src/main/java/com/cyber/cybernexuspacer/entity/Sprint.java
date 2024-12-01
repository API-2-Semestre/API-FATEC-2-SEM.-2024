package com.cyber.cybernexuspacer.entity;

import java.util.Date;

public class Sprint {
    private int id;
    private int numSprint;
    private Date dataInicio;
    private Date dataFim;
    private boolean markedForDeletion;
    private int liberado;

    // Construtores, getters e setters
    public Sprint(int id, int numSprint, Date dataInicio, Date dataFim) {
        this.id = id;
        this.numSprint = numSprint;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.markedForDeletion = false; // Por padrão, não está marcada para exclusão
    }

    public Sprint() {

    }

    public int getId() { return id; }
    public int getNumSprint() { return numSprint; }
    public java.sql.Date getDataInicio() { return (java.sql.Date) dataInicio; }
    public java.sql.Date getDataFim() { return (java.sql.Date) dataFim; }
    public boolean isMarkedForDeletion() { return markedForDeletion; }
    public int getLiberado() { return liberado; }

    public void setId(int id) { this.id = id; }
    public void setNumSprint(int numSprint) { this.numSprint = numSprint; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }
    public void setMarkedForDeletion() {
        this.markedForDeletion = true;
    }
    public void setLiberado(int liberado) { this.liberado = liberado; }

}

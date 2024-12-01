package com.cyber.cybernexuspacer.entity;

public class AcompanharSprints {
    private String nomeAluno;
    private String grupo;
    private double mediaNotaAluno; // Média das notas para o critério e sprint
    private double somaTotalCriterio; // Soma total das notas do critério para todos os alunos no grupo/sprint
    private String criterio;
    private int sprint;

    public AcompanharSprints() {}

    public AcompanharSprints(String nomeAluno, String grupo, double mediaNotaAluno, double somaTotalCriterio) {
        this.nomeAluno = nomeAluno;
        this.grupo = grupo;
        this.mediaNotaAluno = mediaNotaAluno;
        this.somaTotalCriterio = somaTotalCriterio;
    }

    // Getters e Setters
    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public double getMediaNotaAluno() {
        return mediaNotaAluno;
    }

    public void setMediaNotaAluno(double mediaNotaAluno) {
        this.mediaNotaAluno = mediaNotaAluno;
    }

    public double getSomaTotalCriterio() {
        return somaTotalCriterio;
    }

    public void setSomaTotalCriterio(double somaTotalCriterio) {
        this.somaTotalCriterio = somaTotalCriterio;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public int getSprint() {
        return sprint;
    }

    public void setSprint(int sprint) {
        this.sprint = sprint;
    }


}

package model;

import java.time.LocalDate;

public class Emprestimo {
    private int id;
    private int objetoId;
    private int amigoId;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private String status;
    private String observacoes;

    public Emprestimo(
            int id,
            int objetoId,
            int amigoId,
            LocalDate dataEmprestimo,
            LocalDate dataPrevistaDevolucao,
            LocalDate dataDevolucao,
            String status,
            String observacoes
    ) {
        this.id = id;
        this.objetoId = objetoId;
        this.amigoId = amigoId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
        this.observacoes = observacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(int objetoId) {
        this.objetoId = objetoId;
    }

    public int getAmigoId() {
        return amigoId;
    }

    public void setAmigoId(int amigoId) {
        this.amigoId = amigoId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", objetoId=" + objetoId +
                ", amigoId=" + amigoId +
                ", status='" + status + '\'' +
                '}';
    }
}

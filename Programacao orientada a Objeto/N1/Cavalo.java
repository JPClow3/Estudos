package org.poo;

public class Cavalo {
    private int id;
    private String nome;
    private int idade;
    private double peso;
    private double velocidadeMaxima;
    private String raca;

    public Cavalo(int id, String nome, int idade, double peso, double velocidadeMaxima, String raca) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.velocidadeMaxima = velocidadeMaxima;
        this.raca = raca;
    }

    public int id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public int idade() {
        return idade;
    }

    public double peso() {
        return peso;
    }

    public double velocidadeMaxima() {
        return velocidadeMaxima;
    }

    public String raca() {
        return raca;
    }
}
package atividade1804;

public class Bicicleta {
    private String marca;
    private int qtdRodas;
    private String modelo;
    private int velocidade;
    private int numMarchas;
    private boolean bagageiro;

    public Bicicleta(String marca, int qtdRodas, String modelo, int velocidade, int numMarchas, boolean bagageiro) {
        this.marca = marca;
        this.qtdRodas = qtdRodas;
        this.modelo = modelo;
        this.velocidade = velocidade;
        this.numMarchas = numMarchas;
        this.bagageiro = bagageiro;
    }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public int getQtdRodas() { return qtdRodas; }
    public void setQtdRodas(int qtdRodas) { this.qtdRodas = qtdRodas; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getVelocidade() { return velocidade; }
    public void setVelocidade(int velocidade) { this.velocidade = velocidade; }
    public int getNumMarchas() { return numMarchas; }
    public void setNumMarchas(int numMarchas) { this.numMarchas = numMarchas; }
    public boolean isBagageiro() { return bagageiro; }
    public void setBagageiro(boolean bagageiro) { this.bagageiro = bagageiro; }

    public String[] toArray() {
        return new String[] {
            marca,
            String.valueOf(qtdRodas),
            modelo,
            String.valueOf(velocidade),
            String.valueOf(numMarchas),
            String.valueOf(bagageiro)
        };
    }
}

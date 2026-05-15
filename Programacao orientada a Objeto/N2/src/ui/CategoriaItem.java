package ui;

public class CategoriaItem {
    private final int id;
    private final String nome;

    public CategoriaItem(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return nome;
    }
}


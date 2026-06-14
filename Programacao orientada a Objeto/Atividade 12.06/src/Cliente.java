public class Cliente {
    private int id;
    private String nome;
    private int idBicicleta;

    public Cliente(int id, String nome, int idBicicleta) {
        this.id = id;
        this.nome = nome;
        this.idBicicleta = idBicicleta;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdBicicleta() {
        return idBicicleta;
    }
}

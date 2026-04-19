package poo.atv2803;

public class cliente {

    int ID;
    String Nome;
    String Email;
    String Telefone;
    String Cidade;
    String Estado;
    String Data;
    boolean Status;
    String Segmento;
    float Valor;


    public cliente(int ID, String nome, String email, String telefone, String cidade, String estado, String data, boolean status, String segmento, float valor) {
        this.ID = ID;
        Nome = nome;
        Email = email;
        Telefone = telefone;
        Cidade = cidade;
        Estado = estado;
        Data = data;
        Status = status;
        Segmento = segmento;
        Valor = valor;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getSegmento() {
        return Segmento;
    }

    public void setSegmento(String segmento) {
        Segmento = segmento;
    }

    public float getValor() {
        return Valor;
    }

    public void setValor(float valor) {
        Valor = valor;
    }
}

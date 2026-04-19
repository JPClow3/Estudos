package src;
public class cliente {

    private int ID;
    private String Nome;
    private String Email;
    private String Telefone;
    private String Cidade;
    private String Estado;
    private String Data;
    private boolean Status;
    private String Segmento;
    private float Valor;

    public cliente(int ID, String nome, String email, String telefone, String cidade, String estado, String data, boolean status, String segmento, float valor) {
        this.ID = ID;
        this.Nome = nome;
        this.Email = email;
        this.Telefone = telefone;
        this.Cidade = cidade;
        this.Estado = estado;
        this.Data = data;
        this.Status = status;
        this.Segmento = segmento;
        this.Valor = valor;
    }

    public int getID() {
        return ID;
    }

    public String getNome() {
        return Nome;
    }

    public String getEmail() {
        return Email;
    }

    public String getTelefone() {
        return Telefone;
    }

    public String getCidade() {
        return Cidade;
    }

    public String getEstado() {
        return Estado;
    }

    public String getData() {
        return Data;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getSegmento() {
        return Segmento;
    }

    public float getValor() {
        return Valor;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public void setTelefone(String telefone) {
        this.Telefone = telefone;
    }

    public void setCidade(String cidade) {
        this.Cidade = cidade;
    }

    public void setEstado(String estado) {
        this.Estado = estado;
    }

    public void setData(String data) {
        this.Data = data;
    }

    public void setStatus(boolean status) {
        this.Status = status;
    }

    public void setSegmento(String segmento) {
        this.Segmento = segmento;
    }

    public void setValor(float valor) {
        this.Valor = valor;
    }
}
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pessoa {

    private String nome_pessoa;
    private String end_pessoa;
    private String cep_pessoa;
    private String tel_pessoa;
    private double renda_pessoa;
    private int sit_pessoa;

    private final List<Conta_Comum> contas = new ArrayList<>();

    public Pessoa() {}

    public Pessoa(String nome_pessoa, String end_pessoa, String cep_pessoa, String tel_pessoa,
                  double renda_pessoa, int sit_pessoa) {
        this.nome_pessoa = nome_pessoa;
        this.end_pessoa = end_pessoa;
        this.cep_pessoa = cep_pessoa;
        this.tel_pessoa = tel_pessoa;
        this.renda_pessoa = renda_pessoa;
        this.sit_pessoa = sit_pessoa;
    }

    public void adicionarConta(Conta_Comum conta) {
        if (conta == null) throw new IllegalArgumentException("Conta não pode ser null.");
        if (!contas.contains(conta)) contas.add(conta);
    }

    public boolean removerConta(Conta_Comum conta) {
        return contas.remove(conta);
    }

    public List<Conta_Comum> getContas() {
        return Collections.unmodifiableList(contas);
    }

    public String getNome_pessoa() { return nome_pessoa; }
    public void setNome_pessoa(String nome_pessoa) { this.nome_pessoa = nome_pessoa; }

    public String getEnd_pessoa() { return end_pessoa; }
    public void setEnd_pessoa(String end_pessoa) { this.end_pessoa = end_pessoa; }

    public String getCep_pessoa() { return cep_pessoa; }
    public void setCep_pessoa(String cep_pessoa) { this.cep_pessoa = cep_pessoa; }

    public String getTel_pessoa() { return tel_pessoa; }
    public void setTel_pessoa(String tel_pessoa) { this.tel_pessoa = tel_pessoa; }

    public double getRenda_pessoa() { return renda_pessoa; }
    public void setRenda_pessoa(double renda_pessoa) { this.renda_pessoa = renda_pessoa; }

    public int getSit_pessoa() { return sit_pessoa; }
    public void setSit_pessoa(int sit_pessoa) { this.sit_pessoa = sit_pessoa; }
}
public class Pessoa_Fisica extends Pessoa {

    private String cpf_pessoa;
    private String rg_pessoa;

    public Pessoa_Fisica() {}

    public Pessoa_Fisica(String nome_pessoa, String end_pessoa, String cep_pessoa, String tel_pessoa,
                         double renda_pessoa, int sit_pessoa,
                         String cpf_pessoa, String rg_pessoa) {
        super(nome_pessoa, end_pessoa, cep_pessoa, tel_pessoa, renda_pessoa, sit_pessoa);
        this.cpf_pessoa = cpf_pessoa;
        this.rg_pessoa = rg_pessoa;
    }

    public String getCpf_pessoa() { return cpf_pessoa; }
    public void setCpf_pessoa(String cpf_pessoa) { this.cpf_pessoa = cpf_pessoa; }

    public String getRg_pessoa() { return rg_pessoa; }
    public void setRg_pessoa(String rg_pessoa) { this.rg_pessoa = rg_pessoa; }
}
public class Pessoa_Juridica extends Pessoa {

    private String cnpj_pessoa;

    public Pessoa_Juridica() {}

    public Pessoa_Juridica(String nome_pessoa, String end_pessoa, String cep_pessoa, String tel_pessoa,
                           double renda_pessoa, int sit_pessoa,
                           String cnpj_pessoa) {
        super(nome_pessoa, end_pessoa, cep_pessoa, tel_pessoa, renda_pessoa, sit_pessoa);
        this.cnpj_pessoa = cnpj_pessoa;
    }

    public String getCnpj_pessoa() { return cnpj_pessoa; }
    public void setCnpj_pessoa(String cnpj_pessoa) { this.cnpj_pessoa = cnpj_pessoa; }
}
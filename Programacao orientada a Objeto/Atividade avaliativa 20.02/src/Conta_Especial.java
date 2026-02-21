public class Conta_Especial extends Conta_Comum {

    private double limite_conta;

    public Conta_Especial() {}

    public Conta_Especial(String nro_conta, String dt_abertura, String senha, double limite_conta) {
        super(nro_conta, dt_abertura, senha);
        this.limite_conta = limite_conta;
    }
    @Override
    public void sacar(double valor) {
        validarContaAtiva();
        if (valor <= 0) throw new IllegalArgumentException("Saque deve ser > 0.");

        double disponivel = getSaldo() + limite_conta;
        if (valor > disponivel) throw new IllegalArgumentException("Saldo + limite insuficiente.");

        setSaldo(getSaldo() - valor);
        registrarMovimento(Movimento.SAQUE, valor);
    }

    public double getLimite_conta() { return limite_conta; }
    public void setLimite_conta(double limite_conta) { this.limite_conta = limite_conta; }
}
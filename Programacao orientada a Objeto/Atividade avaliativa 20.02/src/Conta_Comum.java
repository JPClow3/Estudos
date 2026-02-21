import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Conta_Comum {

    private String nro_conta;
    private String dt_abertura;
    private String dt_encerramento;
    private int situacao = 1;
    private String senha;
    private double saldo = 0;

    private final List<Movimento> movimentos = new ArrayList<>();

    public Conta_Comum() {}

    public Conta_Comum(String nro_conta, String dt_abertura, String senha) {
        this.nro_conta = nro_conta;
        this.dt_abertura = dt_abertura;
        this.senha = senha;
        this.situacao = 1;
        this.saldo = 0;
    }

    public void depositar(double valor) {
        validarContaAtiva();
        if (valor <= 0) throw new IllegalArgumentException("Deposito deve ser > 0.");
        saldo += valor;
        registrarMovimento(Movimento.DEPOSITO, valor);
    }

    public void sacar(double valor) {
        validarContaAtiva();
        if (valor <= 0) throw new IllegalArgumentException("Saque deve ser > 0.");
        if (valor > saldo) throw new IllegalArgumentException("Saldo insuficiente.");
        saldo -= valor;
        registrarMovimento(Movimento.SAQUE, valor);
    }

    public void encerrar(String dt_encerramento) {
        if (situacao != 1) throw new IllegalStateException("Conta ja nao esta ativa.");
        this.dt_encerramento = dt_encerramento;
        this.situacao = 0;
    }

    protected void registrarMovimento(int tipo, double valor) {
        Date agoraData = new Date();
        Time agoraHora = new Time(System.currentTimeMillis());
        movimentos.add(new Movimento(tipo, agoraData, agoraHora, valor));
    }

    protected void validarContaAtiva() {
        if (situacao != 1) throw new IllegalStateException("Conta nao esta ativa.");
    }

    public List<Movimento> getMovimentos() {
        return Collections.unmodifiableList(movimentos);
    }

    public String getNro_conta() { return nro_conta; }
    public void setNro_conta(String nro_conta) { this.nro_conta = nro_conta; }

    public String getDt_abertura() { return dt_abertura; }
    public void setDt_abertura(String dt_abertura) { this.dt_abertura = dt_abertura; }

    public String getDt_encerramento() { return dt_encerramento; }
    public void setDt_encerramento(String dt_encerramento) { this.dt_encerramento = dt_encerramento; }

    public int getSituacao() { return situacao; }
    public void setSituacao(int situacao) { this.situacao = situacao; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}

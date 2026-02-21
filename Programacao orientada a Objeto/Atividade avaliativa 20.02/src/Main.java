
public class Main {
    public static void main(String[] args) {

        Pessoa_Fisica p1 = new Pessoa_Fisica(
                "João", "Rua X", "75900-000", "64999999999",
                3500.0, 1,
                "123.456.789-00", "MG-12.345.678"
        );

        Conta_Especial ce = new Conta_Especial("0001-9", "2026-02-20", "1234", 500.0);
        p1.adicionarConta(ce);

        ce.depositar(100);
        ce.sacar(200);

        System.out.println("Saldo final: " + ce.getSaldo());
        System.out.println("Movimentos: " + ce.getMovimentos().size());
        System.out.println("Primeiro movimento: " + ce.getMovimentos().get(0).getTipo_mov());
        System.out.println("Segundo movimento: " + ce.getMovimentos().get(1).getTipo_mov());
        System.out.println("Dados da pessoa: " + p1.getNome_pessoa() + ", " + p1.getTel_pessoa());
        System.out.println("Limite da conta: " + ce.getLimite_conta());
        System.out.println("Renda da pessoa: " + p1.getRenda_pessoa());
    }
}
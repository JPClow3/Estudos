import java.util.Locale;

class Student {
    final String id;
    final String nome;
    final Double n1, n2, n3;

    Student(String id, String nome, Double n1, Double n2, Double n3) {
        this.id = id;
        this.nome = nome;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    static Double tryParseDouble(String s) {
        if (s == null) return null;
        String norm = s.trim().replace(',', '.');
        if (norm.isEmpty()) return null;
        try {
            return Double.parseDouble(norm);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    boolean isValid() {
        return n1 != null && n2 != null && n3 != null;
    }

    double media() {
        return (n1 + n2 + n3) / 3.0;
    }

    String situacao(double media) {
        return media >= 6.0 ? "Aprovado" : "Reprovado";
    }

    String formatResult() {
        if (!isValid()) {
            return String.format("%s;%s;;DADOS_INVALIDOS", id, nome);
        }
        double m = media();
        return String.format(Locale.US, "%s;%s;%.2f;%s", id, nome, m, situacao(m));
    }
}
// Java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Path in = resolveInput(args);
        String output = args.length > 1 ? args[1] : "out/alunos_resultado.csv";

        try {
            Path out = Path.of(output);
            if (out.getParent() != null) {
                Files.createDirectories(out.getParent());
            }

            try (BufferedReader br = Files.newBufferedReader(in, StandardCharsets.UTF_8);
                 PrintWriter pw = new PrintWriter(Files.newBufferedWriter(out, StandardCharsets.UTF_8))) {

                pw.println("ID;NOME;MEDIA;SITUACAO");

                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || isHeader(line)) continue;

                    String[] p = line.split(";", -1);
                    // Expected: ID;NOME;TELEFONE;NOTA1;NOTA2;NOTA3
                    if (p.length < 6) {
                        pw.println(";;;" + "DADOS_INVALIDOS");
                        continue;
                    }

                    String id = p[0].trim();
                    String nome = p[1].trim();
                    Double n1 = Student.tryParseDouble(p[3]);
                    Double n2 = Student.tryParseDouble(p[4]);
                    Double n3 = Student.tryParseDouble(p[5]);

                    Student s = new Student(id, nome, n1, n2, n3);
                    pw.println(s.formatResult());
                }
            }

            System.out.println("Processamento concluído. Saída em: " + output);
        } catch (IOException e) {
            System.err.println("Erro ao processar arquivos: " + e.getMessage());
            System.exit(1);
        }
    }

    private static boolean isHeader(String line) {
        String up = line.toUpperCase(Locale.ROOT);
        return up.startsWith("ID;") && up.contains("NOME") && up.contains("NOTA1");
    }

    // Java
    private static Path resolveInput(String[] args) {
        if (args.length > 0) return Path.of(args[0]);
        String[] candidates = {"alunos.txt", "../alunos.txt"};
        for (String c : candidates) {
            Path p = Path.of(c);
            if (Files.exists(p)) return p;
        }
        return Path.of("alunos.txt");
    }
}
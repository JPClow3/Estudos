import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir"), "src/localizar_palavras.txt");
        String filePath = file.getAbsolutePath();
        List<String> palavrasProcuradas = List.of("Função", "Java", "Recursão", "Catch");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                for (String palavra : palavrasProcuradas) {
                    if (linha.contains(palavra)) {
                        System.out.println("Palavra encontrada: " + palavra);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}

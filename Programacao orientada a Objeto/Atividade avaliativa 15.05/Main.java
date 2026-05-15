import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String arquivoCSV = "Livros - 08-05.csv";
        List<Livro> livros = new ArrayList<>();

        // Importando o arquivo
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha = br.readLine(); // Pular o cabeçalho
            
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 4) {
                    String titulo = dados[0];
                    String autor = dados[1];
                    int ano = Integer.parseInt(dados[2]);
                    int paginas = Integer.parseInt(dados[3]);
                    
                    livros.add(new Livro(titulo, autor, ano, paginas));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        // a. Lista de todos os livros
        System.out.println("--- a. Lista de todos os livros ---");
        livros.forEach(System.out::println);
        System.out.println();

        // b. Lista livros separadas por autor
        System.out.println("--- b. Lista de livros separados por autor ---");
        Map<String, List<Livro>> livrosPorAutor = livros.stream()
                .collect(Collectors.groupingBy(Livro::getAutor));
        
        livrosPorAutor.forEach((autor, lista) -> {
            System.out.println("Autor: " + autor);
            lista.forEach(livro -> System.out.println("  - " + livro.getTitulo() + " (" + livro.getAno() + ")"));
        });
        System.out.println();

        // c. Mostrar a média de páginas dos livros
        System.out.println("--- c. Média de páginas dos livros ---");
        double mediaPaginas = livros.stream()
                .mapToInt(Livro::getPaginas)
                .average()
                .orElse(0.0);
        System.out.println("Média de páginas: " + String.format("%.2f", mediaPaginas));
        System.out.println();

        // d. Lista dos livros publicados antes do ano 1950
        System.out.println("--- d. Livros publicados antes do ano 1950 ---");
        List<Livro> livrosAntes1950 = livros.stream()
                .filter(l -> l.getAno() < 1950)
                .collect(Collectors.toList());
        
        if (livrosAntes1950.isEmpty()) {
            System.out.println("Nenhum livro encontrado antes de 1950.");
        } else {
            livrosAntes1950.forEach(System.out::println);
        }
    }
}
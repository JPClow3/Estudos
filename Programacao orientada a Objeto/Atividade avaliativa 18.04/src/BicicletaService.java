package atividade1804;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BicicletaService {
    private final File arquivo;

    public BicicletaService(String caminhoArquivo) {
        this.arquivo = new File(caminhoArquivo);
    }

    public List<Bicicleta> lerBicicletas() {
        List<Bicicleta> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String linha = br.readLine(); // cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 6) {
                    Bicicleta b = new Bicicleta(
                        partes[0],
                        Integer.parseInt(partes[1]),
                        partes[2],
                        Integer.parseInt(partes[3]),
                        Integer.parseInt(partes[4]),
                        Boolean.parseBoolean(partes[5])
                    );
                    lista.add(b);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void salvarBicicletas(List<Bicicleta> lista) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivo, false), StandardCharsets.UTF_8))) {
            bw.write("marca;qtdRodas;modelo;velocidade;numMarchas;bagageiro\n");
            for (Bicicleta b : lista) {
                bw.write(String.join(";", b.toArray()));
                bw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

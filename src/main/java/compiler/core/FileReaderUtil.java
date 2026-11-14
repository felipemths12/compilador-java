package main.java.compiler.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReaderUtil {

    public static String lerArquivo(String caminhoOriginal) {
        //Extrai apenas o nome do arquivo
        String nomeArquivo = Path.of(caminhoOriginal).getFileName().toString();

        //Monta as rotas onde a pasta testes costuma se esconder
        Path[] tentativas = {
                Path.of(caminhoOriginal),                               // Rota 1
                Path.of("testes", nomeArquivo),                         // Rota 2
                Path.of("compilador-java", "testes", nomeArquivo),      // Rota 3
                Path.of("..", "testes", nomeArquivo)                    // Rota 4
        };

        Path caminhoCorreto = null;
        for (Path p : tentativas) {
            if (Files.exists(p)) {
                caminhoCorreto = p;
                System.out.println("[DEBUG] Arquivo ENCONTRADO com sucesso em: " + p.toAbsolutePath());
                break; // Achou o arquivo, para de procurar
            }
        }

        // Se não achou em nenhum lugar, avisa e retorna vazio
        if (caminhoCorreto == null) {
            System.err.println("[ERRO CRÍTICO] O arquivo '" + nomeArquivo + "' não existe em nenhuma das rotas acima.");
            return "";
        }

        try {
            return Files.readString(caminhoCorreto);
        } catch (IOException e) {
            System.err.println("[ERRO CRÍTICO] Falha ao tentar ler o texto do arquivo: " + e.getMessage());
            return "";
        }
    }
}
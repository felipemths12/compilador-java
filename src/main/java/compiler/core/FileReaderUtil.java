package main.java.compiler.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReaderUtil {
    public static String lerArquivo (String caminhoDoArquivo) {
        Path caminho = Path.of(caminhoDoArquivo);

        if (!Files.exists(caminho)) {
            if (caminhoDoArquivo.startsWith("compilador-java/")){
                Path semPrefixo = Path.of(caminhoDoArquivo.replaceFirst("^compilador-java", ""));
                if(Files.exists(semPrefixo)) {
                    caminho = semPrefixo;
                }
            } else {
                Path comPrefixo = Path.of("compilador-java/" + caminhoDoArquivo.replaceFirst("^\\./", ""));
                if (Files.exists(comPrefixo)) {
                    caminho = comPrefixo;
                }
            }
        }

        try {
            return Files.readString(caminho);
        } catch (IOException e) {
            System.err.println("[ERRO CRÍTICO] Não foi possível ler o arquivo fonte: " + caminhoDoArquivo);
            System.err.println("Verifique se o arquivo existe na pasta 'testes' e se o nome está correto");
        }
        return "";
    }
}
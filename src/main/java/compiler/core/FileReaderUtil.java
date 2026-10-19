package main.java.compiler.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReaderUtil {
    public static String lerArquivo (String caminhoDoArquivo) {
        try {
            return Files.readString(Path.of(caminhoDoArquivo));
        } catch (IOException e) {
            System.err.println("[ERRO CRÍTICO] Não foi possível ler o arquivo fonte: " + caminhoDoArquivo);
            System.err.println("Verifique se o arquivo existe na pasta 'testes' e se o nome está correto");
        }
        return "";
    }
}
package main.java.compiler.core;

public class Main {
    public static void main(String[] args) {
        String caminho = "compilador-java/testes/codigo_correto.java";


        System.out.println("Iniciando o compilador...");
        String codigo = FileReaderUtil.lerArquivo(caminho);

        if (!codigo.isEmpty()) {
            System.out.println("Arquivo lido com sucesso!");
            System.out.println("Conteúdo lido: " + codigo);
        }
    }
}

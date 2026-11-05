package main.java.compiler.core;

import main.java.compiler.lexer.Scanner;
import main.java.compiler.lexer.Token;
import main.java.compiler.parser.Parser;
import main.java.compiler.parser.ast.AstNode;
import main.java.compiler.symboltable.SymbolTable;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String caminho = "compilador-java/testes/codigoCorreto.java";


        System.out.println("Iniciando o compilador...");
        String codigo = FileReaderUtil.lerArquivo(caminho);

        if (!codigo.isEmpty()) {
            System.out.println("Arquivo lido com sucesso!");
            System.out.println("Conteúdo lido: " + codigo);
        }

        System.out.println("\nIniciando Análise Léxica");

        SymbolTable tabela = new SymbolTable();

        Scanner scanner = new Scanner(codigo, tabela);
        List<Token> tokens = scanner.analisar();

        System.out.println("\n--- LISTA DE TOKEN ---");
        for(Token t : tokens) {
            System.out.println(t);
        }

        tabela.imprimirTabela();

        System.out.println("\n--- INICIANDO ANÁLISE SINTÁTICA ---");
        Parser parser = new Parser(tokens);
        AstNode arvore = parser.parse();

        if (arvore != null) {
            System.out.println(arvore.imprimir(""));
        } else {
            System.out.println("Nenhuma atribuição válida encontrada ou erro sintático.");
        }
    }
}

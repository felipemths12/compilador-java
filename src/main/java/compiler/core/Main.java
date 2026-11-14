package main.java.compiler.core;

import main.java.compiler.lexer.Scanner;
import main.java.compiler.lexer.Token;
import main.java.compiler.parser.Parser;
import main.java.compiler.parser.ast.AstNode;
import main.java.compiler.semantic.SemanticAnalyzer;
import main.java.compiler.symboltable.SymbolTable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<AstNode> arvores = parser.parse();

        if (!arvores.isEmpty()) {
            for (int i = 0; i < arvores.size(); i++) {
                System.out.println("--- ÁRVORE " + (i +1) + "---");
                System.out.println(arvores.get(i).imprimir("  "));
            }

            System.out.println("--- ANÁLISE SEMÂNTICA ---");

            Set<String> memoriaVariaveis = new HashSet<>();

            for (int i = 0; i < arvores.size(); i++) {
                System.out.println("Validando Árvore " + (i + 1) + ":");
                SemanticAnalyzer semantico = new SemanticAnalyzer(arvores.get(i), memoriaVariaveis);
                semantico.analisar();
                System.out.println();
            }

        } else {
            System.out.println("Nenhuma árvore encontrada.");
        }
    }
}

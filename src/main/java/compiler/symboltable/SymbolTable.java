package main.java.compiler.symboltable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Symbol> tabela = new LinkedHashMap<>();

    public void inserir(String nome, Symbol simbolo) {
        if(!tabela.containsKey(nome)) {
            tabela.put(nome, simbolo);
        }
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public void imprimirTabela() {
        System.out.println("\n--- Tabela de Símbolos ---");
        for(Symbol s : tabela.values()) {
            System.out.println(s);
        }
    }
}

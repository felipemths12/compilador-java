package main.java.compiler.symboltable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Symbol> tabela = new LinkedHashMap<>();
    private Integer proximoId = 1;

    public Integer inserirOuBuscar(String nome) {
        if(!tabela.containsKey(nome)) {
            tabela.put(nome, new Symbol(proximoId, nome));
            proximoId++;
        }
        return tabela.get(nome).id();
    }

    public void imprimirTabela() {
        System.out.println("\n--- Tabela de Símbolos ---");
        for(Symbol s : tabela.values()) {
            System.out.println(s);
        }
    }
}

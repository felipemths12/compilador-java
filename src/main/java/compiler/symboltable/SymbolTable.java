package main.java.compiler.symboltable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Symbol> tabela = new LinkedHashMap<>();
    private Integer proximoId = 1;

    public Integer inserirOuBuscar(String nome) {
        return inserirOuBuscar(nome, "IDENTIFICADOR", "-");
    }

    public Integer inserirOuBuscar(String nome, String categoria, String tipoDado) {
        if (!tabela.containsKey(nome)) {
            tabela.put(nome, new Symbol(proximoId, nome, categoria, tipoDado));
            proximoId++;
        } else {
            Symbol atual = tabela.get(nome);
            if (atual.tipoDado().equals("-") && !tipoDado.equals("-")) {
                tabela.put(nome, new Symbol(atual.id(), atual.nome(), atual.categoria(), tipoDado));
            }
        }
        return tabela.get(nome).id();
    }

    public String buscarNomePorReferencia(String referenciaId) {
        for (Symbol s : tabela.values()) {
            if (("#" + s.id()).equals(referenciaId)) {
                return s.nome();
            }
        }
        return referenciaId;
    }

    public void imprimirTabela() {
        System.out.println("\n--- Tabela de Símbolos ---");
        System.out.println(String.format("%-4s | %-15s | %-18s | %s", "ID", "Lexema", "Categoria", "Tipo"));
        System.out.println("------------------------------------------------------------");
        for (Symbol s : tabela.values()) {
            System.out.println(s);
        }
    }
}
package main.java.compiler.parser.ast;

public record TermoNode(String tipoRegra, String valor) implements AstNode {
    @Override
    public String imprimir(String identacao) {
        return identacao + tipoRegra + " (" + valor + ")";
    }
}

package main.java.compiler.symboltable;

public record Symbol(String nome, String tipo, Integer linhaDeclaracao) {

    @Override
    public String toString() {
        return String.format("[ID: %-10s | Tipo: %-10s | Linha: %d]", nome, tipo, linhaDeclaracao);
    }
}

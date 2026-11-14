package main.java.compiler.symboltable;

public record Symbol(Integer id, String nome, String categoria, String tipoDado) {

    @Override
    public String toString() {
        return String.format("#%-3d | %-15s | %-18s | %s", id, nome, categoria, tipoDado);
    }
}
package main.java.compiler.symboltable;

public record Symbol(Integer id, String nome) {

    @Override
    public String toString() {
        return "#" + id + "," + nome;
    }
}

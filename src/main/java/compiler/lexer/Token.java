package main.java.compiler.lexer;

public record Token(TokenType tipo, String lexema, Integer linha) {

    @Override
    public String toString() {
        return String.format("[Tipo: %-20s, | Lexema: '%s', Linha: %d]", tipo, lexema, linha);
    }
}

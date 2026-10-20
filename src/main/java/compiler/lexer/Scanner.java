package main.java.compiler.lexer;

import main.java.compiler.symboltable.Symbol;
import main.java.compiler.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class Scanner {

    private String codigo;
    private Integer pos = 0;
    private Integer linha = 1;
    private List<Token> tokens = new ArrayList<>();
    private SymbolTable tabelaSimbolos;

    public Scanner(String codigo, SymbolTable tabelaSimbolos) {
        this.codigo = codigo;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    private String lerIdentificador() {
        StringBuilder sb = new StringBuilder();
        while(pos < codigo.length() && Character.isLetterOrDigit(codigo.charAt(pos))) {
            sb.append(codigo.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    private String lerNumero() {
        StringBuilder sb = new StringBuilder();
        while(pos < codigo.length() && Character.isDigit(codigo.charAt(pos))) {
            sb.append(codigo.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    public List<Token> analisar() {
        while (pos < codigo.length()) {
            char c = codigo.charAt(pos);

            //Ignorar espaços e quebras de linha
            if(Character.isWhitespace(c)) {
                if(c == '\n') {
                    linha++;
                }
                pos++;
                continue;
            }

            //Identificar identificadores
            if(Character.isLetter(c)) {
                String lexema = lerIdentificador();
                tokens.add(new Token(TokenType.IDENTIFICADOR, lexema, linha));

                tabelaSimbolos.inserir(lexema, new Symbol(lexema, "VARIAVEL", linha));
                continue;
            }

            if(Character.isDigit(c)) {
                String lexema = lerNumero();
                tokens.add(new Token(TokenType.NUMERO, lexema, linha));
                continue;
            }

            switch (c) {
                case '=':
                    tokens.add(new Token(TokenType.OPERADOR_ATRIBUICAO, "=", linha));
                    pos++;
                    break;

                case '+':
                    tokens.add(new Token(TokenType.OPERADOR_SOMA, "+", linha));
                    pos++;
                    break;

                case '-':
                    tokens.add(new Token(TokenType.OPERADOR_SUBTRACAO, "-", linha));
                    pos++;
                    break;

                case ';':
                    tokens.add(new Token(TokenType.DELIMITADOR, ";", linha));
                    pos++;
                    break;

                default:
                    tokens.add(new Token(TokenType.DESCONHECIDO, String.valueOf(c), linha));
                    pos++;
                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "EOF", linha));
        return tokens;
    }
}

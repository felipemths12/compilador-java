package main.java.compiler.lexer;

import main.java.compiler.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Scanner {

    private String codigo;
    private Integer pos = 0;
    private Integer linha = 1;
    private List<Token> tokens = new ArrayList<>();
    private SymbolTable tabelaSimbolos;

    private static final Set<String> PALAVRAS_RESERVADAS = Set.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
            "true", "false", "null"
    );

    private static final Set<String> TIPOS_DECLARACAO = Set.of(
            "int", "double", "float", "boolean", "char", "byte", "short", "long", "void", "class"
    );

    public Scanner(String codigo, SymbolTable tabelaSimbolos) {
        this.codigo = codigo;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    private String lerIdentificador() {
        StringBuilder sb = new StringBuilder();
        while (pos < codigo.length() && Character.isLetterOrDigit(codigo.charAt(pos))) {
            sb.append(codigo.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    private String lerNumero() {
        StringBuilder sb = new StringBuilder();
        while (pos < codigo.length() && Character.isDigit(codigo.charAt(pos))) {
            sb.append(codigo.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    private String inferirTipoDeclaracao() {
        if (tokens.isEmpty()) return "-";

        Token anterior = tokens.get(tokens.size() - 1);

        if (anterior.tipo() == TokenType.PALAVRA_RESERVADA && TIPOS_DECLARACAO.contains(anterior.lexema())) {
            return anterior.lexema();
        }

        if (anterior.tipo() == TokenType.IDENTIFICADOR) {
            return tabelaSimbolos.buscarNomePorReferencia(anterior.lexema());
        }

        if (tokens.size() >= 3 &&
                anterior.tipo() == TokenType.FECHA_COLCHETE &&
                tokens.get(tokens.size() - 2).tipo() == TokenType.ABRE_COLCHETE) {
            Token tipoBase = tokens.get(tokens.size() - 3);
            String nomeBase = tipoBase.tipo() == TokenType.IDENTIFICADOR
                    ? tabelaSimbolos.buscarNomePorReferencia(tipoBase.lexema())
                    : tipoBase.lexema();
            return nomeBase + "[]";
        }

        return "-";
    }

    public List<Token> analisar() {
        while (pos < codigo.length()) {
            char c = codigo.charAt(pos);

            //Ignorar espaços e quebras de linha
            if (Character.isWhitespace(c)) {
                if (c == '\n') {
                    linha++;
                }
                pos++;
                continue;
            }

            //Identificar identificadores e palavras reservadas
            if (Character.isLetter(c)) {
                String lexema = lerIdentificador();
                if (PALAVRAS_RESERVADAS.contains(lexema)) {
                    tabelaSimbolos.inserirOuBuscar(lexema, "PALAVRA_RESERVADA", "-");
                    tokens.add(new Token(TokenType.PALAVRA_RESERVADA, lexema, linha));
                } else {
                    String tipoInferido = inferirTipoDeclaracao();
                    Integer idTabela = tabelaSimbolos.inserirOuBuscar(lexema, "IDENTIFICADOR", tipoInferido);
                    tokens.add(new Token(TokenType.IDENTIFICADOR, "#" + idTabela, linha));
                }
                continue;
            }

            if (Character.isDigit(c)) {
                String lexema = lerNumero();
                tokens.add(new Token(TokenType.NUMERO, lexema, linha));
                continue;
            }

            if (c == '.') {
                tokens.add(new Token(TokenType.PONTO, ".", linha));
                pos++;
                continue;
            }

            if (c == '"') {
                pos++;
                StringBuilder texto = new StringBuilder();
                while (pos < codigo.length() && codigo.charAt(pos) != '"') {
                    texto.append(codigo.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(TokenType.STRING_LITERAL, texto.toString(), linha));
                pos++;
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
                    tokens.add(new Token(TokenType.PONTO_E_VIRGULA, ";", linha));
                    pos++;
                    break;

                case '{':
                    tokens.add(new Token(TokenType.ABRE_CHAVE, "{", linha));
                    pos++;
                    break;

                case '}':
                    tokens.add(new Token(TokenType.FECHA_CHAVE, "}", linha));
                    pos++;
                    break;

                case '[':
                    tokens.add(new Token(TokenType.ABRE_COLCHETE, "[", linha));
                    pos++;
                    break;

                case ']':
                    tokens.add(new Token(TokenType.FECHA_COLCHETE, "]", linha));
                    pos++;
                    break;

                case '(':
                    tokens.add(new Token(TokenType.ABRE_PARENTESE, "(", linha));
                    pos++;
                    break;

                case ')':
                    tokens.add(new Token(TokenType.FECHA_PARENTESE, ")", linha));
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
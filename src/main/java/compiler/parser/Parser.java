package main.java.compiler.parser;

import main.java.compiler.lexer.Token;
import main.java.compiler.lexer.TokenType;
import main.java.compiler.parser.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser {
    private List<Token> tokens;
    private int pos = 0;

    private static final Set<String> TIPOS_VALIDOS = Set.of("int", "double", "float", "boolean", "char", "byte", "short", "long", "String");
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token tokenAtual() {
        if(pos < tokens.size()) {
            return tokens.get(pos);
        }
        return new Token(TokenType.EOF, "", -1);
    }

    private boolean isTokenTipoDado(Token token) {
        return (token.tipo() == TokenType.PALAVRA_RESERVADA || token.tipo() == TokenType.IDENTIFICADOR
        && TIPOS_VALIDOS.contains(token.lexema()));
    }

    private void avancar() {
        pos++;
    }

    //Regra: <termo> ::= <identificador> | <numero>
    private AstNode parseTermo() {
        Token t = tokenAtual();
        if (t.tipo() == TokenType.IDENTIFICADOR) {
            avancar();

            if (tokenAtual().tipo() == TokenType.PONTO) {
                avancar();

                if (tokenAtual().tipo() == TokenType.IDENTIFICADOR) {
                    String metodo = tokenAtual().lexema();
                    avancar();

                    if (tokenAtual().tipo() == TokenType.ABRE_PARENTESE) {
                        avancar();

                        if (tokenAtual().tipo() == TokenType.FECHA_PARENTESE) {
                            avancar();

                            return new TermoNode("METODO", t.lexema() + "." + metodo + "()");
                        }
                    }
                }
                return null;
            }

            return new TermoNode("ID", t.lexema());
        } else if (t.tipo() == TokenType.NUMERO) {
            avancar();
            return new TermoNode("NUM", t.lexema());
        }

        // Retorna null silenciosamente se achar uma letra/palavra onde devia ter um número,
        // permitindo que o 'parse()' recupere o erro e continue varrendo.
        return null;
    }

    //Regra: <Declaracao>
    private AstNode parseDeclaracao() {
        String tipoDado = tokenAtual().lexema();
        avancar(); //consome a palavra reservada (ex: 'int')

        // Verificação de segurança estrutural
        if (tokenAtual().tipo() != TokenType.IDENTIFICADOR) {
            return null;
        }

        String idNome = tokenAtual().lexema();
        avancar(); //consome a variável

        if (tokenAtual().tipo() != TokenType.OPERADOR_ATRIBUICAO) {
            return null;
        }
        avancar(); //consome o sinal '='

        AstNode expressao = parseExpressao(); // Desce a árvore para resolver a matemática
        if (expressao == null) return null;

        if (tokenAtual().tipo() == TokenType.PONTO_E_VIRGULA) {
            avancar(); // consome ';'
            return new DeclaracaoNode(tipoDado, idNome, expressao);
        }

        return null;
    }

    //Regra: <Expressao> ::= <termo> ("+" | "-") <termo> | <termo>
    private AstNode parseExpressao() {
        AstNode esquerdo = parseTermo();
        if (esquerdo == null) {
            return null;
        }

        if (tokenAtual().tipo() == TokenType.OPERADOR_SOMA || tokenAtual().tipo() == TokenType.OPERADOR_SUBTRACAO) {
            String operador = tokenAtual().lexema();
            avancar(); // consome "+" ou "-"

            AstNode direito = parseTermo();
            if (direito == null) return null;

            return new ExpressaoNode(esquerdo, operador, direito);
        }
        return esquerdo;
    }

    //Regra: <Atribuicao> ::= <identificador> "=" <Expressao> ";"
    private AstNode parseAtribuicao() {
        String idNome = tokenAtual().lexema();
        avancar(); // consome o identificador
        avancar(); // consome o "="

        AstNode expressao = parseExpressao(); // Desce na gramática
        if (expressao == null) {
            return null;
        }

        if (tokenAtual().tipo() == TokenType.PONTO_E_VIRGULA) {
            avancar(); // consome o ";"
            return new AtribuicaoNode(idNome, expressao);
        }

        return null;
    }

    public List<AstNode> parse() {
        List<AstNode> arvoresEncontradas = new ArrayList<>();
        while(tokenAtual().tipo() != TokenType.EOF) {

            //Declaração de variável com tipo
            if (isTokenTipoDado(tokenAtual())) {
                int posicaoSalva = pos; // Salva o estado atual
                AstNode arvoreDeclaracao = parseDeclaracao();

                if (arvoreDeclaracao != null) {
                    arvoresEncontradas.add(arvoreDeclaracao); //Guarda a arvore
                    continue;
                } else {
                    pos = posicaoSalva; // Volta a posição salva
                }
            }

            //Atribuição simples sem tipo
            else if (tokenAtual().tipo() == TokenType.IDENTIFICADOR) {
                if (pos + 1 < tokens.size() && tokens.get(pos + 1).tipo() == TokenType.OPERADOR_ATRIBUICAO) {
                    int posicaoSalva = pos; // Salva o estado atual
                    AstNode tentativa = parseAtribuicao();

                    if (tentativa != null) {
                        arvoresEncontradas.add(tentativa);
                        continue;
                    } else {
                        pos = posicaoSalva;
                    }
                }
            }
            avancar();
        }
        return arvoresEncontradas;
    }
}

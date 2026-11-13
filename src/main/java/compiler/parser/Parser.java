package main.java.compiler.parser;

import main.java.compiler.lexer.Token;
import main.java.compiler.lexer.TokenType;
import main.java.compiler.parser.ast.*;

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

    private void avancar() {
        pos++;
    }

    //Regra: <Termo>
    private AstNode parseTermo() {
        Token t = tokenAtual();
        if(t.tipo() == TokenType.IDENTIFICADOR) {
            avancar();
            return new TermoNode("ID", t.lexema());
        } else if (t.tipo() == TokenType.NUMERO) {
            avancar();
            return new TermoNode("NUM", t.lexema());
        } else {
            System.err.println("Erro Sintático: Esperado ID ou NUM.");
            return null;
        }
    }

    //Regra: <Declaracao>
    private AstNode parseDeclaracao() {
        String tipoDado = tokenAtual().lexema();
        avancar();

        String idNome = tokenAtual().lexema();
        avancar();

        avancar();

        AstNode expressao = parseExpressao();

        if (tokenAtual().tipo() == TokenType.PONTO_E_VIRGULA) {
            avancar();
            return new DeclaracaoNode(tipoDado, idNome, expressao);
        } else {
            System.err.println("Erro: Faltou ';' na declaração.");
            return null;
        }
    }

    //Regra: <Expressao>
    private AstNode parseExpressao() {
        AstNode esquerdo = parseTermo();

        if(tokenAtual().tipo() == TokenType.OPERADOR_SOMA || tokenAtual().tipo() == TokenType.OPERADOR_SUBTRACAO) {
            String operador = tokenAtual().lexema();
            avancar();
            AstNode direito = parseTermo();
            return new ExpressaoNode(esquerdo, operador, direito);
        }
        return esquerdo;
    }

    //Regra: <Atribuicao>
    private AstNode parseAtribuicao() {
        String idNome = tokenAtual().lexema();
        avancar();
        avancar();

        AstNode expressao = parseExpressao();
        if(tokenAtual().tipo() == TokenType.PONTO_E_VIRGULA) {
            avancar();
            return new AtribuicaoNode(idNome, expressao);
        } else {
            System.err.println("Erro Sintático: Esperado ';' no final da atribuição.");
            return null;
        }
    }

    public AstNode parse() {
        while(tokenAtual().tipo() != TokenType.EOF) {
            if(tokenAtual().tipo() == TokenType.IDENTIFICADOR) {
                if(pos + 1 < tokens.size() && tokens.get(pos + 1).tipo() == TokenType.OPERADOR_ATRIBUICAO) {

                    //ponto de retorno
                    int posicaoSalva = pos;

                    //tentativa de montar a árvore
                    AstNode tentativa = parseAtribuicao();

                    if (tentativa != null) {
                        return tentativa; //conseguiu montar a árvore
                    } else {
                        pos = posicaoSalva; //não conseguiu e retorna ao ponto de retorno
                    }

                    return parseAtribuicao();
                }
            }
            avancar();
        }
        return null;
    }
}

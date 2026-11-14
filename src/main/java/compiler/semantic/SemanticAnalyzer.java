package main.java.compiler.semantic;

import main.java.compiler.parser.ast.*;
import main.java.compiler.symboltable.SymbolTable;

import java.util.Set;

public class SemanticAnalyzer {
    private AstNode arvore;
    private boolean codigoValido = true;
    private Set<String> variaveisDeclaradas;
    private SymbolTable tabelaSimbolos;

    public SemanticAnalyzer(AstNode arvore, Set<String> variaveisDeclaradas, SymbolTable tabelaSimbolos) {
        this.arvore = arvore;
        this.variaveisDeclaradas = variaveisDeclaradas;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    public SemanticAnalyzer(AstNode arvore, Set<String> variaveisDeclaradas) {
        this(arvore, variaveisDeclaradas, null);
    }

    private String formatarNome(String idRef) {
        if (tabelaSimbolos != null) {
            String nomeReal = tabelaSimbolos.buscarNomePorReferencia(idRef);
            return "'" + nomeReal + "' (" + idRef + ")";
        }
        return "'" + idRef + "'";
    }

    public boolean analisar() {
        if (arvore == null) return false;
        validarNo(arvore);
        if (codigoValido) {
            System.out.println("  [OK] Nenhum erro semântico encontrado.");
        }
        return codigoValido;
    }

    private void validarNo(AstNode no) {
        if (no instanceof DeclaracaoNode declaracao) {
            validarNo(declaracao.expressao());
            variaveisDeclaradas.add(declaracao.id());

        } else if (no instanceof AtribuicaoNode atribuicao) {
            String idDestino = atribuicao.id();
            if (!variaveisDeclaradas.contains(idDestino)) {
                System.out.println("  [ERRO SEMÂNTICO] Variável " + formatarNome(idDestino) + " utilizada não foi declarada previamente.");
                codigoValido = false;
            } else {
                System.out.println("  [Semântico] Variável de destino verificada e existente: " + formatarNome(idDestino));
            }
            validarNo(atribuicao.expressao());

        } else if (no instanceof ExpressaoNode expressao) {
            validarNo(expressao.esquerdo());
            validarNo(expressao.direito());

        } else if (no instanceof TermoNode termo) {
            if (termo.tipoRegra().equals("ID")) {
                String idRef = termo.valor();

                if (!variaveisDeclaradas.contains(idRef)) {
                    System.out.println("  [ERRO SEMÂNTICO] Variável " + formatarNome(idRef) + " utilizada não foi declarada previamente.");
                    codigoValido = false;
                } else {
                    System.out.println("  [Semântico] Variável verificada e existente: " + formatarNome(idRef));
                }
            }
        }
    }
}
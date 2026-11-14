package main.java.compiler.semantic;

import main.java.compiler.parser.ast.*;

import java.util.Set;

public class SemanticAnalyzer {
    private AstNode arvore;
    private Boolean codigoValido = true;

    private Set<String> variaveisDeclaradas; //armazenar variáveis já declaradas

    public SemanticAnalyzer(AstNode arvore, Set<String> variaveisDeclaradas) {
        this.arvore = arvore;
        this.variaveisDeclaradas = variaveisDeclaradas;
    }
    
    private void validarNo(AstNode no) {

        //Regra de Declaração
        if (no instanceof DeclaracaoNode declaracao) {
            variaveisDeclaradas.add(declaracao.id());
            validarNo(declaracao.expressao());
        }
        //Regra de Atribuição
        else if (no instanceof AtribuicaoNode atribuicao) {
            validarNo(atribuicao.expressao());
        } else if (no instanceof ExpressaoNode expressao) {
            validarNo(expressao.esquerdo());
            validarNo(expressao.direito());
        } else if (no instanceof TermoNode termo) {
            if (termo.tipoRegra().equals("ID")) {
                String idRef = termo.valor();

                if (!variaveisDeclaradas.contains(idRef)) {
                    System.out.println("  [ERRO SEMÂNTICO] Variável não encontrada: " + idRef);
                    codigoValido = false;
                } else {
                    System.out.println("  [Semântico] Variável verificada e existente: " + idRef);
                }
            }
        }
    }

    public boolean analisar() {
        if (arvore == null) {
            return false;
        }

        validarNo(arvore);

        if (codigoValido) {
            System.out.println("  [OK] Nenhum erro semântico encontrado.");
        }

        return codigoValido;
    }
}

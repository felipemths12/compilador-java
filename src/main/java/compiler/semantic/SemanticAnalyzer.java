package main.java.compiler.semantic;

import main.java.compiler.parser.ast.AstNode;
import main.java.compiler.parser.ast.AtribuicaoNode;
import main.java.compiler.parser.ast.ExpressaoNode;
import main.java.compiler.parser.ast.TermoNode;

public class SemanticAnalyzer {
    private AstNode arvore;
    private Boolean codigoValido = true;
    
    private void validarNo(AstNode no) {
        if (no instanceof AtribuicaoNode atribuicao) {
            validarNo(atribuicao.expressao());
        } else if (no instanceof ExpressaoNode expressao) {
            validarNo(expressao.esquerdo());
            validarNo(expressao.direito());
        } else if (no instanceof TermoNode termo) {
            if (termo.tipoRegra().equals("ID")) {
                String idRef = termo.valor();

                if (idRef == null || idRef.isEmpty()) {
                    System.err.println("  [ERRO SEMÂNTICO] Variável corrompida ou não encontrada");
                    codigoValido = false;
                } else {
                    System.out.println("  [Semântico] Validando integridade e acesso à variável: " + idRef);
                }
            }
        }
    }

    public void analisar() {
        if (arvore == null) {
            return;
        }

        validarNo(arvore);

        if (codigoValido) {
            System.out.println("  [OK] Nenhum erro semântico encontrado.");
        }
    }
}

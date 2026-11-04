package main.java.compiler.parser.ast;

public record ExpressaoNode(AstNode esquerdo, String operador, AstNode direito) implements AstNode {
    @Override
    public String imprimir(String identacao) {
        StringBuilder sb =  new StringBuilder();
        //Imprime ele mesmo
        sb.append(identacao).append("Expressao (").append(operador).append(")\n");
        //Imprime o nó da esquerda
        sb.append(esquerdo.imprimir(identacao + "  ")).append("\n");
        //Imprime o nó da direita
        sb.append(direito.imprimir(identacao + "  "));

        return sb.toString();
    }
}

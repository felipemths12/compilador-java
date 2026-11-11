package main.java.compiler.parser.ast;

public record AtribuicaoNode(String id, AstNode expressao) implements AstNode {
    @Override
    public String imprimir(String identacao) {
        StringBuilder sb = new StringBuilder();
        sb.append(identacao).append("Atribuicao\n");
        sb.append(identacao).append("  ID (").append(id).append(")\n");
        sb.append(identacao).append("  OP. ATRIB (=)\n");
        sb.append(expressao.imprimir(identacao + "  "));

        return sb.toString();
    }
}

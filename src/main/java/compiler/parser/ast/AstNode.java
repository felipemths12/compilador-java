package main.java.compiler.parser.ast;

import java.util.ArrayList;
import java.util.List;

public class AstNode {
    private String label;
    private String value;
    private List<AstNode> children;

    public AstNode(String label, String value) {
        this.label = label;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(AstNode child) {
        if (child != null) {
            this.children.add(child);
        }
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public List<AstNode> getChildren() {
        return children;
    }
}

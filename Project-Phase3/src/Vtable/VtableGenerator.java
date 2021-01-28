package Vtable;

import AST.ASTNode;
import codegen.SimpleVisitor;

import java.util.ArrayList;
import java.util.List;

public class VtableGenerator implements SimpleVisitor {

    private List<Function> functions = new ArrayList<>();

    @Override
    public void visit(ASTNode node) throws Exception {
        switch (node.getNodeType()) {
            default :
                visitAllChildren(node);
        }
    }
    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }
}

package codegen;

import AST.ASTNode;

public interface SimpleVisitor {
    void visit(ASTNode node) throws Exception;
}

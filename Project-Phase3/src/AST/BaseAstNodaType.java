package AST;


import codegen.SimpleVisitor;
import semantic.SymbolInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base node type of AST
 */
public class BaseASTNode implements ASTNode {
    private List<ASTNode> children = new ArrayList<>();
    private ASTNode parent;
    private NodeType nodeType;
    SymbolInfo symbolInfo;

    public BaseASTNode(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setSymbolInfo(SymbolInfo si) {
        this.symbolInfo = si;
    }

    public SymbolInfo getSymbolInfo() {
        return symbolInfo;
    }

    @Override
    public String toString() {
        String str = nodeType.toString();

        if (symbolInfo != null) {
            str += " (" + symbolInfo.toString() + ")";
        }

        return str;
    }

    @Override
    public void accept(SimpleVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public void addChild(ASTNode node) {
        children.add(node);
    }

    @Override
    public void addChild(int index, ASTNode node) {
        children.add(index, node);
    }

    @Override
    public void addChildren(List<ASTNode> nodes) {
        children.addAll(nodes);
    }

    @Override
    public void addChild(ASTNode... nodes) {
        Collections.addAll(children, nodes);
    }

    @Override
    public void setChildren(ASTNode... nodes) {
        children = Arrays.asList(nodes);
    }

    @Override
    public ASTNode getChild(int index) {
        return children.get(index);
    }

    @Override
    public List<ASTNode> getChildren() {
        return children;
    }

    @Override
    public ASTNode getParent() {
        return parent;
    }

    @Override
    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
}

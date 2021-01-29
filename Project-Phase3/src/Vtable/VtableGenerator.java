package Vtable;

import AST.*;
import codegen.CodeGenVisitor;
import codegen.SimpleVisitor;
import codegen.SymbolInfo;
import codegen.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class VtableGenerator implements SimpleVisitor {

    public static List<Function> functions = new ArrayList<>();
    public static List<ClassDecaf> classes = new ArrayList<>();
    private SymbolTable symbolTable = new SymbolTable();

    @Override
    public void visit(ASTNode node) throws Exception {
        switch (node.getNodeType()) {
            case BOOLEAN_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case DOUBLE_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.DOUBLE));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case INT_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.INT));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case STRING_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.STRING));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case VOID:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.VOID));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case IDENTIFIER:
                IdentifierNode idNode = (IdentifierNode) node;
                node.setSymbolInfo(new SymbolInfo(node, new IdentifierType(idNode.getValue())));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
                break;
            case METHOD_DECLARATION:
                visitMethodDeclarationNode(node);
                break;
            case ARGUMENTS:
                visitArgumentsNode(node);
                break;
            case START:
                visitStartNode(node);
                break;
            case VARIABLE_DECLARATION:
                visitVariableDeclaration(node);
                break;
            case Class_DECLARATION:
                visitClassDeclaration(node);
                break;
            case FIELD_DECLARATION:
                visitFieldDeclaration(node);
                break;
            default:
                visitAllChildren(node);
        }
    }

    private void visitFieldDeclaration(ASTNode node) throws Exception {
        AccessMode accessMode = AccessMode.Public;
        if (node.getChild(0).getNodeType().equals(NodeType.METHOD_ACCESS)) {
            switch (node.getChild(0).getChild(0).getNodeType()) {
                case PRIVATE_ACCESS:
                    accessMode = AccessMode.Private;
                    break;
                case PUBLIC_ACCESS:
                    accessMode = AccessMode.Public;
                    break;
                case PROTECTED_ACCESS:
                    accessMode = AccessMode.Protected;
                    break;
            }
            node.getChild(1).accept(this);
        } else
            node.getChild(0).accept(this);

    }

    private void visitClassDeclaration(ASTNode node) throws Exception {
        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        String className = idNode.getValue();
        ClassDecaf classDecaf = new ClassDecaf(className);
        if (node.getChild(1).getNodeType().equals(NodeType.EXTEND)) {
            IdentifierNode idNode1 = (IdentifierNode) node.getChild(1).getChild(0);
            classDecaf.setParentClassName(idNode1.getValue());
        }
        ClassDecaf.currentClass = classDecaf;
        classes.add(classDecaf);
        symbolTable.enterScope(classDecaf.getName());
        if (node.getChild(node.getChildren().size() - 1).getNodeType().equals(NodeType.FIELDS)) {
            node.getChild(node.getChildren().size() - 1).accept(this);
            ClassDecaf.currentClass.setObjectSize(ClassDecaf.currentClass.getFields().size() * 4);
        }
        symbolTable.leaveCurrentScope();

    }

    private void visitVariableDeclaration(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String fieldName = idNode.getValue();
        if (symbolTable.getCurrentScopeName().equals(ClassDecaf.currentClass.getName())) {
            Field field = new Field(fieldName);
            field.setSymbolInfo(node.getSymbolInfo());
            field.setAccessMode(Field.getCurrentAccessMode());
            field.setClassDecaf(ClassDecaf.currentClass);
            if (ClassDecaf.currentClass.getFields().contains(field))
                throw new Exception(fieldName + " declared before");
            else
                ClassDecaf.currentClass.getFields().add(field);
        }
//        symbolTable.enterScope(label);
//        node.getChild(2).accept(this);
//        symbolTable.leaveCurrentScope();
//        if (symbolTable.getCurrentScopeName().equals("global")) {
//            method.setAccessMode(AccessMode.Public);
//        } else {
//            method.setAccessMode(Field.currentAccessMode);
//            ClassDecaf.currentClass.getMethods().add(method);
//        }
    }

    private void visitStartNode(ASTNode node) throws Exception {
        symbolTable.enterScope("global");
        visitAllChildren(node);
        boolean isMainExist = false;
        for (Function function : functions) {
            if (function.getName().equals("main") && function.getScope().getName().equals("global")) {
                if (function.getArgumentsType().isEmpty() && function.getReturnType().getType().getAlign() == 4) {
                    isMainExist = true;
                    break;
                }
            }
        }
        if (!isMainExist)
            throw new Exception("main does not exist");
        for (ClassDecaf aClass : classes) {
            if (!aClass.getParentClassName().equals("")) {
                for (ClassDecaf bclassDecaf : classes) {
                    if (aClass.getParentClassName().equals(bclassDecaf.getName())) {
                        aClass.setParentClass(bclassDecaf);
                        for (Field field : bclassDecaf.getFields()) {
                            if (aClass.getFields().contains(field))
                                throw new Exception("the variable declared in parent class");
                            else
                                aClass.getFields().addAll(bclassDecaf.getFields());
                        }

                    }
                }
                if (aClass.getParentClass() == null)
                    throw new Exception("parent does not exist");
            }
        }
        System.out.println(classes);
    }

    private void visitMethodDeclarationNode(ASTNode node) throws Exception {
        node.getChild(0).accept(this); //Type
        SymbolInfo returnType = node.getChild(0).getSymbolInfo();

        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();
        Function method = new Function(methodName, returnType, symbolTable.getCurrentScope());
        if (functions.contains(method)) {
            throw new Exception(methodName + " function declared before");
        }
        functions.add(method);

        Function.currentFunction = method;
        String label = symbolTable.getCurrentScopeName() + "_" + methodName;
        symbolTable.enterScope(label);
        node.getChild(2).accept(this);
        symbolTable.leaveCurrentScope();
        if (symbolTable.getCurrentScopeName().equals("global")) {
            method.setAccessMode(AccessMode.Public);
        } else {
            method.setAccessMode(Field.currentAccessMode);
            ClassDecaf.currentClass.getMethods().add(method);
        }
    }

    private void visitArgumentsNode(ASTNode node) throws Exception {
        int argumentsLen = node.getChildren().size() * (-4);
        Function function = Function.currentFunction;
        for (int i = argumentsLen / (-4); i >= 1; i--) {
            ASTNode ArgumentNode = node.getChild(i - 1);
            ArgumentNode.accept(this);
            function.getArgumentsType().add(ArgumentNode.getChild(0).getSymbolInfo());
        }
    }

    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    private void setParentSymbolInfo(ASTNode node, ASTNode child) throws Exception {
        child.accept(this);
        Type type = child.getSymbolInfo().getType();
        SymbolInfo si = new SymbolInfo(node, type);
        si.setDimensionArray(child.getSymbolInfo().getDimensionArray());
        node.setSymbolInfo(si);
    }
}

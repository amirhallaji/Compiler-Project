package codegen;

import AST.*;


import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import AST.ClassNode;


/**
 * An AST visitor which generates Jasmin code.
 */
public class CodeGenVisitor implements SimpleVisitor {
    private PrintStream stream;
    private int labelIndex;
    private String className;
    private ClassNode classNode;
    private boolean returnGenerated;

    private SymbolTable symbolTable = new SymbolTable();
    private int blockIndex;
    // Fix all of the FIXMEs below.

    List<String> regs = Arrays.asList(
            "zero", "at", "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1",
            "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4",
            "s5", "s6", "t7", "t8", "t9", "k0", "k1", "gp", "sp", "fp", "ra"
    );

    private static String dataSegment = ".data \n";
    private static String textSegment = ".text \n" + "\t.globl main\n";

    public CodeGenVisitor(PrintStream stream) {
        this.stream = stream;
    }


    @Override
    public void visit(ASTNode node) throws Exception {
        switch (node.getNodeType()) {
            case ADDITION:
                break;
            case SUBTRACTION:
                break;
            case MULTIPLICATION:
                break;
            case DIVISION:
                break;
            case MOD:
                break;
            case NEGATIVE:
                break;
            case READ_INTEGER:
                break;
            case READ_LINE:
                break;
            case NEW_ARRAY:
                break;
            case NEW_IDENTIFIER:
                break;
            case ITOB:
                break;
            case ITOD:
                break;
            case DTOI:
                break;
            case BTOI:
                break;
            case LVALUE:
                visitLValueNode(node);
                break;
            case CALL:
                break;
            case EMPTY_ARRAY:
                break;
            case LESS_THAN:
                break;
            case LESS_THAN_OR_EQUAL:
                break;
            case GREATER_THAN:
                break;
            case GREATER_THAN_OR_EQUAL:
                break;
            case EQUAL:
                break;
            case NOT_EQUAL:
                break;
            case BOOLEAN_AND:
                break;
            case BOOLEAN_NOT:
                break;
            case BOOLEAN_OR:
                break;
            case BOOLEAN_TYPE:
                break;
            case DOUBLE_TYPE:
                break;
            case CHAR_TYPE:
                break;
            case INT_TYPE:
                break;
            case FLOAT_TYPE:
                break;
            case LONG_TYPE:
                break;
            case STRING_TYPE:
                break;
            case VOID:
                break;
            case AUTO_TYPE:
                break;
            case FIELD_DECLARATION:
                //TODO
                break;
            case LOCAL_VAR_DECLARATION:
                break;
            case VARIABLE_DECLARATION:
                visitVariableDeclaration(node);
                break;
            case VARIABLE_CONST_DECLARATION:
                break;
            case METHOD_DECLARATION:
                visitMethodDeclarationNode(node);
                break;
            case Class_DECLARATION:
                break;
            case DECLARATIONS:
                break;
            case ASSIGN:
                visitAssignNode(node);
                break;
            case ADD_ASSIGN:
                break;
            case SUB_ASSIGN:
                break;
            case MULT_ASSIGN:
                break;
            case DIV_ASSIGN:
                break;
            case STATEMENT:
                visitStatementNode(node);
                break;
            case STATEMENTS:
                visitStatementsNode(node);
                break;
            case EXPRESSION_STATEMENT:
                visitExpressionNode(node);
                break;
            case BREAK_STATEMENT:
                break;
            case CONTINUE_STATEMENT:
                break;
            case RETURN_STATEMENT:
                break;
            case IF_STATEMENT:
                break;
            case REPEAT_STATEMENT:
                break;
            case SWITCH_STATEMENT:
                break;
            case CASE_STATEMENT:
                break;
            case FOR_STATEMENT:
                break;
            case WHILE_STATEMENT:
                break;
            case PRINT_STATEMENT:
                break;
            case LITERAL:
                break;
            case ARGUMENT:
                break;
            case ARGUMENTS:
                visitArgumentsNode(node);
                break;
            case EMPTY_STATEMENT:
                break;
            case IDENTIFIER:
                break;
            case METHOD_ACCESS:
                break;
            case PRIVATE_ACCESS:
                break;
            case PUBLIC_ACCESS:
                break;
            case PROTECTED_ACCESS:
                break;
            case VARIABLES:
                visitAllChildren(node);
                //TODO
                break;
            case ACTUALS:
                break;
            case PARAMETER:
                break;
            case PARAMETERS:
                break;
            case BLOCK:
                visitBlockNode(node);
                break;
            case VAR_USE:
                break;
            case CLASS:
                break;
            case Interface:
                break;
            case NULL_LITERAL:
                break;
            case EXTEND:
                break;
            case IMPLEMENT:
                break;
            case FIELDS:
                //TODO
                break;
            case PROTOTYPES:
                break;
            case PROTOTYPE:
                break;
            case START:
                symbolTable.enterScope("global");
                visitAllChildren(node);
                stream.print(dataSegment + '\n' + textSegment);
                break;
            default:
                visitAllChildren(node);
        }
    }

    private void visitLValueNode(ASTNode node) {
        System.err.println(node.getChildren());
    }

    private void visitAssignNode(ASTNode node) throws Exception {
//        System.err.println(node);
        IdentifierNode idNode = (IdentifierNode) node.getChild(0).getChild(0).getChild(0);

//        visitAllChildren(node);
//
//        ExpressionNode rightSide = (ExpressionNode) node.getChild(1);
//
//        ExpressionNode resultOfCast = cast(leftSide.getType().getPrimitive(), rightSide);
//
//        stream.println("\tstore " + resultOfCast.getType() + " " + rightSide.getResultName() + ", " + resultOfCast.getType() + "* %" + idNode.getValue() + ", align " + resultOfCast.getType().getAlign());
//
//        System.out.println("assign children are " + node.getChildren());
//
//
//        SymbolInfo si = idNode.getSymbolInfo();
//
//        if (si == null)
//            throw new Exception(idNode.getValue() + " not declared");
//
//        /* Expression node */
//        node.getChild(1).accept(this);
//
////        System.err.println(node.getChildren());
    }

    private void visitExpressionNode(ASTNode node) throws Exception {
//        System.err.println(node.getChildren());
        visitAllChildren(node);

    }

    private void visitStatementNode(ASTNode node) throws Exception {
//        System.err.println(node.getChildren());
        visitAllChildren(node);

    }

    private void visitStatementsNode(ASTNode node) throws Exception {
        visitAllChildren(node);
//        System.err.println(node.getChildren());
    }

    private void visitArgumentsNode(ASTNode node) {
//        System.err.println(node.getChildren());
    }

    private void visitBlockNode(ASTNode node) throws Exception {
        if (node.getParent().getNodeType() != NodeType.METHOD_DECLARATION) {
            symbolTable.enterScope("" + blockIndex++);
            visitAllChildren(node);
            symbolTable.leaveScopeType(blockIndex - 1 + "");
        } else {
            System.out.println(node.getChildren());
            visitAllChildren(node);
        }
    }

    private void visitVariableDeclaration(ASTNode node) throws Exception {
        PrimitiveType typePrimitive = (PrimitiveType) (((TypeNode) node.getChild(0)).getType());
        boolean isArray = true;

        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String varName = idNode.getValue();

        if (node.getChild(0).getChildren().isEmpty()) {
            isArray = false;



            ASTNode parent = node.getParent();
            NodeType parentType = parent.getNodeType();

//            if (parentType.equals(NodeType.START))
//                label = "global_" + varName + ":";
//            else if (parentType.equals(NodeType.FIELD_DECLARATION)) {
//                IdentifierNode idNode1 = (IdentifierNode) parent.getParent().getParent().getChild(0);
//                String varName1 = idNode1.getValue();
//                label = varName1 + "_" + varName + ":";
//            } else if (parentType.equals(NodeType.VARIABLES)) {
//                ASTNode func_parent = parent.getParent().getParent();
//                System.out.println(func_parent);
//                IdentifierNode idNode1 = (IdentifierNode) func_parent.getChild(1);
//                String varName1 = idNode1.getValue();
//
//                ASTNode parentOfParent = func_parent.getParent();
//                if (func_parent.getParent().getNodeType().equals(NodeType.START))
//                    label = "global_" + varName1 + "_" + varName + ":";
//                else if (func_parent.getParent().getNodeType().equals(NodeType.FIELD_DECLARATION)) {
//                    IdentifierNode idNode2 = (IdentifierNode) parentOfParent.getParent().getParent().getChild(1);
//                    String varName2 = idNode2.getValue();
//                    label = varName2 + "_" + varName1 + "_" + varName + ":";
//                }
//            }
            label = symbolTable.getCurrentScopeName() + "_" + varName;
            dataSegment += "\t" + label + "\t" + typePrimitive.getSignature() + "\t" + typePrimitive.getPrimitive().getInitialValue() + "\n";

        }
        typePrimitive.setArray(isArray);
        SymbolInfo si = new SymbolInfo(idNode);
        si.setType(typePrimitive);
        idNode.setSymbolInfo(si);


        symbolTable.put(varName, si);
        System.out.println(varName + " .... " + symbolTable.get(varName));
    }

    private void visitUnaryOperation() {
    }

    private void visitBinaryOperation(ASTNode node) {

        ExpressionNode parent = (ExpressionNode) node.getParent();


//        String result = "";
//
//        switch (resultType){
//            case BOOL:
//                break;
//            case INT:
//                break;
//            case DOUBLE:
//                break;
//            case STRING:
//                break;
//
//        }


    }


    private void visitMethodDeclarationNode(ASTNode node) throws Exception {

        TypeNode returnType = (TypeNode) node.getChild(0);
        String returnSig = returnType.getType().getSignature();

        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();

        //type
        symbolTable.enterScope(symbolTable.getCurrentScopeName() + "_" + methodName);


        String label;

        if (node.getParent().getNodeType().equals(NodeType.START))
            label = methodName + ":";
        else {
            IdentifierNode ClassNode = (IdentifierNode) node.getParent().getParent().getParent().getChild(0);
            String className = ClassNode.getValue();
            label = className + "_" + methodName + ":";
        }

        textSegment += "\t" + label + "\n";

        node.getChild(2).accept(this);
        node.getChild(3).accept(this);

        symbolTable.leaveScopeType(blockIndex - 1 + "");
        textSegment += "\t\tjr $ra\n";
    }

    private void visitArgumentNode(ASTNode child) throws Exception {

    }

    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
//            System.out.println("#" + child);
            child.accept(this);
        }
    }

    private String getBlock() {
        return "" + blockIndex++;
    }
}
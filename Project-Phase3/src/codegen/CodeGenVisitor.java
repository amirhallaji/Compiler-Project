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
    private static String textSegment = ".text \n" + "      .globl main\n";

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
        visitAllChildren(node);

//        System.err.println(node.getChildren());
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
            symbolTable.enterScopeType("" + blockIndex++);
            visitAllChildren(node);
            symbolTable.leaveScopeType(blockIndex - 1 + "");
        } else {
            visitAllChildren(node);
        }
    }

    private void visitVariableDeclaration(ASTNode node) {

        System.out.println(node);
        System.out.println(node.getChildren());

        NodeType typeOfData = node.getChild(0).getNodeType();
        System.out.println(typeOfData);

        Type typePrimitive = ((TypeNode) node.getChild(0)).getType();
        System.out.println(typePrimitive);


        switch (typeOfData) {
            case INT_TYPE:

        }
        System.out.println(node.getChild(0));
        String label;

        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String varName = idNode.getValue();
        System.out.println(varName);

        if (node.getParent().getNodeType().equals(NodeType.START))
            label = node.getChild(0) + ":";


        System.out.println(node.getChild(1) + "       .word ");

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
        //type
        System.out.println("node:" + node);
        TypeNode returnType = (TypeNode) node.getChild(0);
        String returnSig = returnType.getType().getSignature();
        System.out.println(returnSig);
        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();
        System.out.println(methodName);
        String label;
        System.out.println(node.getParent());
        if (node.getParent().getNodeType().equals(NodeType.START))
            label = methodName + ":";
        else {
            IdentifierNode ClassNode = (IdentifierNode) node.getParent().getParent().getParent().getChild(0);
            String className = ClassNode.getValue();
            label = className + "_" + methodName + ":";
        }
        System.out.println("la " + label);
        textSegment += label + "\n";
        node.getChild(2).accept(this);
        node.getChild(3).accept(this);
        visitAllChildren(node);
        System.err.println(node.getChildren());
    }

    private void visitArgumentNode(ASTNode child) throws Exception {
        System.out.println(child.getChildren());
    }

    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            System.out.println("#"+child);
            child.accept(this);
        }
    }
}
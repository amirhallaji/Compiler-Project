package codegen;

import AST.ClassNode;
import codegen.SimpleVisitor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AST.ASTNode;
import AST.BooleanLiteralNode;
import AST.ClassNode;
import AST.IdentifierNode;
import AST.IntegerLiteralNode;
import AST.TypeNode;
import semantic.SymbolInfo;
//import cse431.lab5.semantic.SymbolInfo;
//import cse431.lab5.visitor.SimpleVisitor;

/**
 * An AST visitor which generates Jasmin code.
 */
public class CodeGenVisitor implements SimpleVisitor {
    private PrintStream stream;
    private int labelIndex;
    private String className;
    private ClassNode classNode;
    private boolean returnGenerated;
    // Fix all of the FIXMEs below.


    public CodeGenVisitor(PrintStream stream) {
        this.stream = stream;
    }


    @Override
    public void visit(ASTNode node) throws Exception {
        switch (node.getNodeType()) {
            case ADDITION:
                visitAdditionNode(node);
                break;
//
//            case ASSIGN:
//                visitAssignNode(node);
//                break;

            case BOOLEAN_AND:
                visitBooleanAndNode(node);
                break;

//            case BOOLEAN_LITERAL:
//                visitBooleanLiteralNode(node);
//                break;

            case BOOLEAN_NOT:
                visitBooleanNotNode(node);
                break;

            case BOOLEAN_OR:
                visitBooleanOrNode(node);
                break;

            case CLASS:
                visitClassNode(node);
                break;

            case DIVISION:
                visitDivisionNode(node);
                break;

            case EQUAL:
                visitEqualNode(node);
                break;

            case GREATER_THAN:
                visitGreaterThanNode(node);
                break;

            case GREATER_THAN_OR_EQUAL:
                visitGreaterThanOrEqualNode(node);
                break;

            case IF_STATEMENT:
                visitIfStatementNode(node);
                break;

//            case INTEGER_LITERAL:
//                visitIntegerLiteralNode(node);
//                break;

            case LESS_THAN:
                visitLessThanNode(node);
                break;

            case LESS_THAN_OR_EQUAL:
                visitLessThanOrEqualNode(node);
                break;

            case METHOD_ACCESS:
                visitMethodAccessNode(node);
                break;

//            case METHOD_DECLARATION:
//                visitMethodDeclarationNode(node);
//                break;

            case MULTIPLICATION:
                visitMultiplicationNode(node);
                break;

            case NOT_EQUAL:
                visitNotEqualNode(node);
                break;

            case PARAMETER:
                visitParameterNode(node);
                break;

            case RETURN_STATEMENT:
                visitReturnStatementNode(node);
                break;

            case SUBTRACTION:
                visitSubtractionNode(node);
                break;

            case UNARY_MINUS:
                visitUnaryMinusNode(node);
                break;

            case UNARY_PLUS:
                visitUnaryPlusNode(node);
                break;

            case VAR_USE:
                visitVarUse(node);
                break;

            case WHILE_STATEMENT:
                visitWhileStatementNode(node);
                break;

            case ARGUMENTS:
            case BLOCK:
            case BOOLEAN_TYPE:
//            case CAST:
//            case CHAR_LITERAL:
            case CHAR_TYPE:
//            case CLASS_BODY:
//            case COMPILATION_UNIT:
            case DECLARATIONS:
                getDecl(node);
                break;
            case DOUBLE_TYPE:
            case EMPTY_STATEMENT:
            case EXPRESSION_STATEMENT:
            case FIELD_DECLARATION:
//            case FLOAT_LITERAL:
            case IDENTIFIER:
            case INT_TYPE:
            case LOCAL_VAR_DECLARATION:
            case NULL_LITERAL:
            case PARAMETERS:
            case POST_DECREMENT:
            case POST_INCREMENT:
            case PRE_DECREMENT:
            case PRE_INCREMENT:
//            case STRING_LITERAL:
            case VARIABLE_DECLARATION:
//            case VARIABLE_DECLARATIONS:
            case VOID:
            default:
                visitAllChildren(node);
        }
    }

    private void getDecl(ASTNode node) {
        System.out.println("ALLOC");
    }

    ////////////////////  My Helpers  /////////////////////////////

    private int getVarIndexFromIDNode(ASTNode idNode)
    {
        return ((IdentifierNode) idNode).getSymbolInfo().getLocalVarIndex();
    }

    private int getValueFromIntNode(ASTNode intLitNode)
    {
        return ((IntegerLiteralNode) intLitNode).getValue();
    }
    ////////////////////////////////////////////
    ////////////////////////////////////////////
    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    private void visitAdditionNode(ASTNode node) throws Exception {
        visitAllChildren(node);
        stream.println("  iadd");

    }

    //Assigns thing at top of stack,
    //OR if it's a literal, pushes the literal then assigns that val
    //OR if it's an ID loads the ID's value and assigns
//    private void visitAssignNode(ASTNode node) throws Exception {
//        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
//        SymbolInfo si = idNode.getSymbolInfo();
//        int lvIndex = si.getLocalVarIndex();
//
//        List<ASTNode> children = node.getChildren();
//
//        //Push vals of the R value node(s)?
//        for(int i =1; i< children.size(); i++)
//        {
//            children.get(i).accept(this);
//        }
//        stream.println("  istore "+lvIndex);
//
//    }

    private void visitBooleanAndNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitBooleanLiteralNode(ASTNode node) {
        BooleanLiteralNode boolNode = (BooleanLiteralNode) node;
        if(boolNode.getValue())
        {
            stream.println("  iconst_1");
        }
        else{
            stream.println("  iconst_0");
        }
    }

    private void visitBooleanNotNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitBooleanOrNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitClassNode(ASTNode node) throws Exception {
        classNode = (ClassNode) node;

        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        className = idNode.getValue();

        stream.println(".class public " + className);
        stream.println(".super java/lang/Object");
        stream.println("");

        outputDefaultConstructor();
        outputMainMethod();
        outputPrintlnMethod();

        returnGenerated = false;

        node.getChild(1).accept(this);

    }

    private void visitDivisionNode(ASTNode node) throws Exception {
        visitAllChildren(node);
        stream.println("  idiv");
    }

    private void visitEqualNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitGreaterThanNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitGreaterThanOrEqualNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitIfStatementNode(ASTNode node) throws Exception {
        stream.println("; if statement");
        node.getChild(0).accept(this); //predicate
        stream.println("  iconst_0");
        String endIfLabel = generateLabel();
        stream.println("ifeq "+endIfLabel);//if predicate false, then skip the "do if true" block


        node.getChild(0).accept(this); //print the "do if true" block
        stream.println("  goto "+ endIfLabel); //bypass the "do If True" Block

        if(node.getChildren().size()==3) //We have an else statement
        {
            String elseLabel = generateLabel();
            stream.println(elseLabel); //The else block
            node.getChild(2).accept(this);
        }

    }

    private void visitIntegerLiteralNode(ASTNode node) {
        int val =  getValueFromIntNode(node);
        stream.println("  ldc "+val);
    }

    private void visitLessThanNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitLessThanOrEqualNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitMethodAccessNode(ASTNode node) throws Exception {
        node.getChild(1).accept(this);

        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        String methodName = idNode.getValue();
        String sig = classNode.getMethodSig(methodName);
        stream.println("  invokestatic " + className + "/" + sig);
        returnGenerated = false;
    }

//    private void visitMethodDeclarationNode(ASTNode node) throws Exception {
//        IdentifierNode idNode =  node.getChild(0);
//        String methodName = idNode.getValue();
//
//        TypeNode typeNode = (TypeNode) node.getChild(2);
//        String returnType = typeNode.getType().getSignature();
//
//        stream.println("");
//        stream.println(";");
//        stream.println("; method");
//        stream.println(";");
//
//        stream.print(".method public static " + methodName + "(");
//        node.getChild(1).accept(this);
//        stream.println(")" + returnType);
//
//        stream.println("  .limit locals 10");
//        stream.println("  .limit stack 10");
//
//        node.getChild(3).accept(this);
//
//        if (!returnGenerated) {
//            stream.println("  return");
//            returnGenerated = true;
//        }
//
//        stream.println(".end method");
//    }

    private void visitMultiplicationNode(ASTNode node) throws Exception {
        visitAllChildren(node);
        stream.println("  imul");
    }

    private void visitNotEqualNode(ASTNode node) throws Exception {
        // FIXME
    }

    private void visitParameterNode(ASTNode node) throws Exception {
        TypeNode typeNode = (TypeNode) node.getChild(1);
        String typeSig = typeNode.getType().getSignature();
        stream.print(typeSig);
        returnGenerated = false;
    }

    private void visitReturnStatementNode(ASTNode node) throws Exception {
        visitAllChildren(node);

        returnGenerated = true;
        if(node.getChildren().size() == 0 )
        {
            stream.println("  return");
        }
        else{
            stream.println("  ireturn");
        }
    }

    private void visitSubtractionNode(ASTNode node) throws Exception {
        visitAllChildren(node);
        stream.println("  isub");
    }

    private void visitUnaryMinusNode(ASTNode node) throws Exception {
        visitAllChildren(node);
        stream.println("  ineg");
    }

    private void visitUnaryPlusNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitVarUse(ASTNode node) {
        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        SymbolInfo si = idNode.getSymbolInfo();
        int lvIndex = si.getLocalVarIndex();
        stream.println("  iload " + lvIndex);
        returnGenerated = false;
    }

    private void visitWhileStatementNode(ASTNode node) throws Exception {
        // FIXME
    }

    private String generateLabel() {
        return "label" + (++labelIndex);
    }

    private void outputDefaultConstructor() {
        stream.println("");
        stream.println(";");
        stream.println("; standard constructor");
        stream.println(";");
        stream.println(".method public <init>()V");
        stream.println("  aload_0");
        stream.println("  invokenonvirtual java/lang/Object/<init>()V");
        stream.println("  return");
        stream.println(".end method");
        stream.println("");
    }

    private void outputMainMethod() {
        stream.println("");
        stream.println(";");
        stream.println("; main method");
        stream.println(";");
        stream.println(".method public static main([Ljava/lang/String;)V");
        stream.println("  invokestatic " + className + "/program()V");
        stream.println("  return");
        stream.println(".end method");
        stream.println("");
    }

    private void outputPrintlnMethod() {
        stream.println("");
        stream.println(";");
        stream.println("; println method");
        stream.println(";");
        stream.println(".method public static println(I)V");
        stream.println("  .limit stack 2");
        stream.println("  getstatic java/lang/System/out Ljava/io/PrintStream;");
        stream.println("  iload_0");
        stream.println("  invokevirtual java/io/PrintStream/println(I)V");
        stream.println("  return");
        stream.println(".end method");
        stream.println("");
    }
}
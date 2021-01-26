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

    private int tempRegsNumber = 8;
    List<String> regs = Arrays.asList(
            "$zero", "$at", //0
            "$v0", "$v1", //2
            "$a0", "$a1", "$a2", "$a3", //4
            "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", //8
            "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", //16
            "$t7", "$t8", "$t9", //23
            "$k0", "$k1", //26
            "$gp", "$sp", "fp", "ra" //28
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
                visitAdditionNode(node);
                break;
            case SUBTRACTION:
                visitSubtractionNode(node);
                break;
            case MULTIPLICATION:
                visitMultiplicationNode(node);
                break;
            case DIVISION:
                visitDivisionNode(node);
                break;
            case MOD:
                visitModNode(node);
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
                visitStartNode(node);

                break;
            default:
                visitAllChildren(node);
        }
    }

    private void visitModNode(ASTNode node) {
    }

    private void visitDivisionNode(ASTNode node) {
    }

    private void visitMultiplicationNode(ASTNode node) {
    }

    private void visitSubtractionNode(ASTNode node) throws Exception {
        ExpressionNode first = (ExpressionNode) node.getChild(0);
        ExpressionNode second = (ExpressionNode) node.getChild(1);
        operations(first, second, "sub");
        visitAllChildren(node);
    }

    private void visitAdditionNode(ASTNode node) throws Exception {
        ExpressionNode first = (ExpressionNode) node.getChild(0);
        ExpressionNode second = (ExpressionNode) node.getChild(1);
        operations(first, second, "add");
        visitAllChildren(node);
    }

    private void operations(ExpressionNode first, ExpressionNode second, String op) {
        boolean firstIsNegative = (first.getChild(0).getNodeType() == NodeType.NEGATIVE);
        String op2 = firstIsNegative ? "sub" : "add";
        first = firstIsNegative ? (ExpressionNode) first.getChild(0).getChild(0) : first;

        if (first.getChild(0).getNodeType() == NodeType.LITERAL && second.getChild(0).getNodeType() == NodeType.LITERAL) {
            System.err.println("4444444444");
            textSegment += "\t\tli " + regs.get(tempRegsNumber + 1) + ", " + first.getChild(0) + "\n";
            textSegment += "\t\tli " + regs.get(tempRegsNumber + 2) + ", " + second.getChild(0) + "\n";
            textSegment += "\t\t" + op2 + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n"; //check
            textSegment += "\t\t" + op + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 2) + "\n";
        } else if (first.getChild(0).getNodeType() == NodeType.LITERAL) {
            System.err.println("55555555555");
            textSegment += "\t\tli " + regs.get(tempRegsNumber + 1) + ", " + first.getChild(0) + "\n";
            textSegment += "\t\t" + op2 + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n"; //check
            if (second.getChild(0).getNodeType() == NodeType.LVALUE) {
                textSegment += "\t\tlw " + regs.get(tempRegsNumber + 1) + ", " + ((IdentifierNode) second.getChild(0).getChild(0)).getValue() + "\n";
                textSegment += "\t\t" + op + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n";
            }
        } else if (second.getChild(0).getNodeType() == NodeType.LITERAL) {
            System.err.println("66666666666");
            textSegment += "\t\tli " + regs.get(tempRegsNumber + 1) + ", " + second.getChild(0) + "\n";
            textSegment += "\t\t" + op + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n";
            if (first.getChild(0).getNodeType() == NodeType.LVALUE) {
                textSegment += "\t\tlw " + regs.get(tempRegsNumber + 1) + ", " + ((IdentifierNode) first.getChild(0).getChild(0)).getValue() + "\n";
                textSegment += "\t\t" + op2 + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n";  //check
            }
        } else if (first.getChild(0).getNodeType() == NodeType.LVALUE && second.getChild(0).getNodeType() == NodeType.LVALUE) {
            System.err.println("777777777777");
            textSegment += "\t\tlw " + regs.get(tempRegsNumber + 1) + ", " + ((IdentifierNode) first.getChild(0).getChild(0)).getValue() + "\n";
            textSegment += "\t\tlw " + regs.get(tempRegsNumber + 2) + ", " + ((IdentifierNode) second.getChild(0).getChild(0)).getValue() + "\n";
            textSegment += "\t\t" + op2 + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n";  //check
            textSegment += "\t\t" + op + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 2) + "\n";
        } else if (first.getChild(0).getNodeType() == NodeType.LVALUE) {
            System.err.println("8888888888888");
            textSegment += "\t\tlw " + regs.get(tempRegsNumber + 1) + ", " + ((IdentifierNode) first.getChild(0).getChild(0)).getValue() + "\n";
            textSegment += "\t\t" + op2 + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n"; //check
            if (second.getChild(0).getNodeType() == NodeType.LITERAL) {
                textSegment += "\t\tli " + regs.get(tempRegsNumber + 1) + ", " + second.getChild(0) + "\n";
                textSegment += "\t\t" + op + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n";
            }
        } else if (second.getChild(0).getNodeType() == NodeType.LVALUE) {
            System.err.println("9999999999999");
            textSegment += "\t\tlw " + regs.get(tempRegsNumber + 1) + ", " + ((IdentifierNode) second.getChild(0).getChild(0)).getValue() + "\n";
            textSegment += "\t\t" + op + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n";
            if (first.getChild(0).getNodeType() == NodeType.LITERAL) {
                textSegment += "\t\tli " + regs.get(tempRegsNumber + 1) + ", " + first.getChild(0) + "\n";
                textSegment += "\t\t" + op2 + " " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber + 1) + "\n"; //check
            }
        } else {
            System.err.println("hoooooooooooooooooo");
        }
    }


    private void visitLValueNode(ASTNode node) throws Exception {
        System.err.println("errrrrr");

    }

    private void visitAssignNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitExpressionNode(ASTNode node) throws Exception {
        tempRegsNumber = 8;
        visitAllChildren(node);
    }

    private void visitStatementNode(ASTNode node) throws Exception {
        System.err.println(node);

        visitAllChildren(node);
    }

    private void visitStatementsNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitArgumentsNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }
    private void visitStartNode(ASTNode node) throws Exception {
        symbolTable.enterScope("global");
        visitAllChildren(node);
        stream.print(dataSegment + '\n' + textSegment);
    }



//    private void visitArgumentsNode(ASTNode node) throws Exception {
//        int argumentsLen = node.getChildren().size() * (-4);
//        System.out.println(argumentsLen);
//        textSegment += "\t\t addi $sp,$sp," + argumentsLen + "\n";
//
//        visitAllChildren(node);
//        System.out.println(node.getChild(0).getChild(0).getChildren());
//        int stackDepth = 0;
//        for (int i = argumentsLen / (-4); i >= 1; i--) {
//
//            IdentifierNode idNode = (IdentifierNode) node.getChild(i - 1).getChild(0).getChild(1);
//            String idName = idNode.getValue();
//            SymbolInfo si = (SymbolInfo) symbolTable.get(idName);
//
//            if (si.getType().getAlign() == 10) {
//
//                textSegment += "\t\t lw $t1," + i * (-4) + "($sp)\n";
//
//            } else {
//                PrimitiveType typePrimitive = (PrimitiveType) (((TypeNode) node.getChild(0)).getType());
//                if (typePrimitive.getSignature().equals("")) ;
//            }
//
//
//            System.out.println(idName);
//            textSegment += "\t\t sw $s1," + i * 4 + "($sp),";
//
//        }
//        textSegment += "\t\t sw $ra,0($sp)," + argumentsLen + "\n";
//    }

    private void visitBlockNode(ASTNode node) throws Exception {
        if (node.getParent().getNodeType() != NodeType.METHOD_DECLARATION) {
            symbolTable.enterScope("" + blockIndex++);
            visitAllChildren(node);
            symbolTable.leaveScopeType(blockIndex - 1 + "");
        } else {
            visitAllChildren(node);
        }
    }

    private void visitVariableDeclaration(ASTNode node) throws Exception {
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String varName = idNode.getValue();
        String label = symbolTable.getCurrentScopeName() + "_" + varName;

        boolean isArray = true;
        if (node.getChild(0).getChildren().isEmpty())
            isArray = false;

        if (!node.getChild(0).getNodeType().equals(NodeType.IDENTIFIER)) {
            PrimitiveType typePrimitive = (PrimitiveType) (((TypeNode) node.getChild(0)).getType());

            if (!isArray && !typePrimitive.getSignature().equals("ascii")) {

                ASTNode parent = node.getParent();

                dataSegment += "\t" + label + "\t" + typePrimitive.getSignature() + "\t" + typePrimitive.getPrimitive().getInitialValue() + "\n";

            }
            typePrimitive.setArray(isArray);
            SymbolInfo si = new SymbolInfo(idNode);
            si.setType(typePrimitive);
            idNode.setSymbolInfo(si);
            symbolTable.put(varName, si);
        } else {
            IdentifierNode typeIdNode = (IdentifierNode) node.getChild(0);
            String idName = typeIdNode.getValue();
            IdentifierType identifierType = new IdentifierType(idName, 10);

            dataSegment += "\t" + label + "\t" + ".word" + "\t" + 0 + "\n";

            identifierType.setArray(isArray);
            SymbolInfo si = new SymbolInfo(idNode);
            si.setType(identifierType);
            idNode.setSymbolInfo(si);
            symbolTable.put(varName, si);
        }

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


        if (methodName.equals("main") && node.getParent().getNodeType().equals(NodeType.START)) {
            node.getChild(3).accept(this);
            textSegment += "\t\tli $v0,10\n\t\tsyscall\n";
        } else {
            node.getChild(2).accept(this);
            node.getChild(3).accept(this);
            textSegment += "\t\t lw $ra 0($sp)\n";
            textSegment += "\t\tjr $ra\n";
        }
        symbolTable.leaveScopeType(symbolTable.getCurrentScopeName() + "_" + methodName);
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
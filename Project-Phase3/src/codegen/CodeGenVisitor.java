package codegen;

import AST.*;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Vtable.ClassDecaf;
import Vtable.Function;
import AST.ClassNode;
import Vtable.VtableGenerator;


/**
 * An AST visitor which generates Jasmin code.
 */
public class CodeGenVisitor implements SimpleVisitor {
    private PrintStream stream;
    private int labelIndex;
    private String className;
    private ClassNode classNode;
    private boolean returnGenerated;
    private List<Function> functions = VtableGenerator.functions;
    private List<ClassDecaf> classes = VtableGenerator.classes;
    private HashMap<String, String> stringLiterals = new HashMap<>();
    private SymbolTable symbolTable = new SymbolTable();
    private int blockIndex;
    private int arrayNumbers = 0;


    private int tempLiteralCounter = 0;
    private int tempLabelCounter = 0;
    private int tempRegsNumber = 8;
    private int tempfRegsNumber = 0;
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

    List<String> fregs = Arrays.asList(
            "$f0", "$f1", "$f2", "$f3", "$f4", "$f5", "$f6", "$f7", "$f8",
            "$f9", "$f10", "$f11", "$f12", "$f13", "$f14", "$f15", "$f16",
            "$f17", "$f18", "$f19", "$f20", "$f21", "$f22", "$f23", "$f24", "$f25",
            "$f26", "$f27", "$f28", "$f29", "$f30", "$f31"
    );


    private static String dataSegment = ".data \n\ttrue: .asciiz \"true\"\n\tfalse : .asciiz \"false\"\n\n";
    private static String textSegment = "";

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
                break;
            case NEGATIVE:
                visitNegative(node);
                break;
            case READ_INTEGER:
                visitReadIntegerNode(node);
                break;
            case READ_LINE:
                break;
            case NEW_ARRAY:
                visitNewArrayNode(node);
                break;
            case NEW_IDENTIFIER:
                break;
            case ITOB:
                visitItoB(node);
                break;
            case ITOD:
                visitItoD(node);
                break;
            case DTOI:
                visitDtoI(node);
                break;
            case BTOI:
                visitBtoI(node);
                break;
            case LVALUE:
                visitLValueNode(node);
                break;
            case CALL:
                visitCallNode(node);
                break;
            case EMPTY_ARRAY:
                break;
            case LESS_THAN:
                visitLessThanNode(node);
                break;
            case LESS_THAN_OR_EQUAL:
                visitLessThanEqualNode(node);
                break;
            case GREATER_THAN:
                visitGreaterThanNode(node);
                break;
            case GREATER_THAN_OR_EQUAL:
                visitGreaterThanEqualNode(node);
                break;
            case EQUAL:
                visitEqualNode(node);
                break;
            case NOT_EQUAL:
                visitNotEqualNode(node);
                break;
            case BOOLEAN_AND:
                visitAndNode(node);
                break;
            case BOOLEAN_NOT:
                visitNotNode(node);
                break;
            case BOOLEAN_OR:
                visitOrNode(node);
                break;
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
            case FIELD_DECLARATION:
                //TODO
                visitAllChildren(node);
                break;
            case VARIABLE_DECLARATION:
                visitVariableDeclaration(node);
                break;
            case METHOD_DECLARATION:
                visitMethodDeclarationNode(node);
                break;
            case Class_DECLARATION:
                visitClassDeclarationNode(node);
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
                visitReturnNode(node);
                break;
            case IF_STATEMENT:
                visitIfStatement(node);
                break;
            case REPEAT_STATEMENT:
                break;
            case SWITCH_STATEMENT:
                break;
            case CASE_STATEMENT:
                break;
            case FOR_STATEMENT:
                visitForNode(node);
                break;
            case WHILE_STATEMENT:
                visitWhileNode(node);
                break;
            case PRINT_STATEMENT:
                visitPrintNode(node);
                break;
            case LITERAL:
                visitLiteralNode(node);
                break;
            case ARGUMENT:
                break;
            case ARGUMENTS:
                visitArgumentsNode(node);
                break;
            case EMPTY_STATEMENT:
                break;
            case IDENTIFIER:
                IdentifierNode idNode = (IdentifierNode) node;
                node.setSymbolInfo(new SymbolInfo(node, new IdentifierType(idNode.getValue())));
                node.getSymbolInfo().setDimensionArray(node.getChildren().size());
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
                visitClassDeclarationNode(node);
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
                visitAllChildren(node);
                //TODO
                break;
            case PROTOTYPES:
                break;
            case PROTOTYPE:
                break;
            case EXPRESSIONS:
                visitAllChildren(node);
                break;
            case START:
                visitStartNode(node);
                break;
            default:
                visitAllChildren(node);
        }
    }

    private void visitBtoI(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 1)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.INT);
    }

    private void visitItoB(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 4)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.BOOL);
    }

    private void visitDtoI(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 8)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.INT);
        textSegment += "\t\tcvt.w.s $f0 $f0\n";
        textSegment += "\t\tmfc1 $t0 $f0\n";
    }

    private void visitItoD(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 4)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        node.getSymbolInfo().setType(PrimitiveType.DOUBLE);

        textSegment += "\t\tmtc1 $t0 $f0\n";
        textSegment += "\t\tcvt.s.w $f0 $f0\n";

    }

    private void visitOrNode(ASTNode node) throws Exception {
        LogicalOp(node, "or");
    }

    private void LogicalOp(ASTNode node, String op) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 1)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }

        textSegment += "\t\t" + "move $t1" + ", " + "$t0" + "\n";
        textSegment += "\t\t" + "sw " + "$t1" + ", 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";

        setParentSymbolInfo(node, node.getChild(1));

        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\t" + "lw " + "$t1" + ", 0($sp)\n";

        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo())) {
            textSegment += "\t\t" + op + " $t1, $t1, $t0\n";
        }

        textSegment += "\t\t" + "move $t0, $t1\n";
    }

    private void visitNotNode(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 1)) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }
        textSegment += "\t\t" + "xori $t1, $t1, 1\n";
        textSegment += "\t\t" + "move $t0, $t1\n";
    }

    private void visitAndNode(ASTNode node) throws Exception {
        LogicalOp(node, "and");
    }

    private void LogicalOp2(ASTNode node, String type) throws Exception {

        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo first = node.getSymbolInfo();
        int firstType = first.getType().getAlign();

        if (!(node.getChild(0).getSymbolInfo().getType().getAlign() == 4 || node.getChild(0).getSymbolInfo().getType().getAlign() == 8)  && (!(type.equals("ne") || type.equals("eq")))) {
            throw new Exception("Invalid type for " + node.getNodeType().toString() + " operation");
        }


        int tempReg = firstType != 8 ? tempRegsNumber : tempfRegsNumber;
        List<String> reg = firstType != 8 ? regs : fregs;

        String op = firstType != 8 ? "s" + type + " " : "c." + type + ".s ";
        String op2 = firstType != 8 ? "move " : "mov.s ";
        String op3 = firstType != 8 ? "sw " : "swc1 ";
        String op4 = firstType != 8 ? "lw " : "lwc1 ";

        textSegment += "\t\t" + op2 + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
        textSegment += "\t\t" + op3 + reg.get(tempReg + 1) + ", 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";


        setParentSymbolInfo(node, node.getChild(1));
        SymbolInfo second = node.getSymbolInfo();
        String secondType = second.getType().getSignature();

        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\t" + op4 + reg.get(tempReg + 1) + " 0($sp)\n";

        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo())) {
            if (node.getChild(0).getSymbolInfo().getType().getAlign() == 8) {
                switch (op) {
                    case "c.gt.s ":
                        textSegment += "\t\t" + "c.lt.s " + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
                        break;
                    case "c.ge.s ":
                        textSegment += "\t\t" + "c.le.s " + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
                        break;
                    case "c.ne.s ":
                        textSegment += "\t\t" + "c.eq.s " + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
                        break;
                    default:
                        textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
                }

                textSegment += "\t\tbc1f L_CondFalse"+tempLabelCounter+"\n";
                if (!op.equals("c.ne.s ")) {
                    textSegment += "\t\tli $t0 1\n";
                }else {
                    textSegment += "\t\tli $t0 0\n";
                }
                textSegment += "\t\tj L_CondEnd"+tempLabelCounter+"\n";
                if (!op.equals("c.ne.s ")) {
                    textSegment += "\t\tL_CondFalse" +tempLabelCounter+" : li $t0 0\n";
                }else {
                    textSegment += "\t\tL_CondFalse" + tempLabelCounter + ": li $t0 1\n";
                }
                textSegment += "\t\tL_CondEnd" + tempLabelCounter++ +":\n";
            } else {
                textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
            }
            node.getSymbolInfo().setType(PrimitiveType.BOOL);
        } else {
            throw new Exception("Type " + firstType + " & " + secondType + " are mismatched");
        }
        textSegment += "\t\t" + op2 + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
    }

    private void visitGreaterThanEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "ge");
//        node.getChild(0).accept(this);
//        switch (node.getChild(0).getSymbolInfo().getType().getAlign()) {
//            case 4: //int
//                textSegment += "\t\tmove $t1, $t0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tsge $t0, $t0, $t1\n";
//                break;
//            case 8: //double //TODO
//                textSegment += "\t\tmov.s $f1, $f0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tc.le.s $f1, $f0\n";
//                textSegment += "\t\tmovf $t0, $zero\n";
//                break;
//            default:
//                throw new Exception("invalid types for equal");
//        }
//        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo()))
//            node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
//        else
//            throw new Exception("types does not match");
    }

    private void visitGreaterThanNode(ASTNode node) throws Exception {
        LogicalOp2(node, "gt");
//        node.getChild(0).accept(this);
//        switch (node.getChild(0).getSymbolInfo().getType().getAlign()) {
//            case 4: //int
//                textSegment += "\t\tmove $t1, $t0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tsgt $t0, $t0, $t1\n";
//                break;
//            case 8: //double //TODO
//                textSegment += "\t\tmov.s $f1, $f0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tc.lt.s $f1, $f0\n";
//                textSegment += "\t\tmovf $t0, $zero\n";
//                break;
//            default:
//                throw new Exception("invalid types for equal");
//        }
//        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo()))
//            node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
//        else
//            throw new Exception("types does not match");

    }

    private void visitLessThanEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "le");
//        node.getChild(0).accept(this);
//        switch (node.getChild(0).getSymbolInfo().getType().getAlign()) {
//            case 4: //int
//                textSegment += "\t\tmove $t1, $t0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tsle $t0, $t0, $t1\n";
//                break;
//            case 8: //double //TODO
//                textSegment += "\t\tmov.s $f1, $f0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tc.le.s $f0, $f1\n";
//                textSegment += "\t\tmovf $t0, $zero\n";
//                break;
//            default:
//                throw new Exception("invalid types for equal");
//        }
//        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo()))
//            node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
//        else
//            throw new Exception("types does not match");
    }

    private void visitLessThanNode(ASTNode node) throws Exception {
        LogicalOp2(node, "lt");

//        node.getChild(0).accept(this);
//        switch (node.getChild(0).getSymbolInfo().getType().getAlign()) {
//            case 4: //int
//                textSegment += "\t\tmove $t1, $t0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tslt $t0, $t0, $t1\n";
//                break;
//            case 8: //double //TODO
//                textSegment += "\t\tmov.s $f1, $f0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tc.lt.s $f0, $f1\n";
//                textSegment += "\t\tmovf $t0, $zero\n";
//                break;
//            default:
//                throw new Exception("invalid types for equal");
//        }
//        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo()))
//            node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
//        else
//            throw new Exception("types does not match");
    }

    private void visitNotEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "ne");

//        node.getChild(0).accept(this);
//        switch (node.getChild(0).getSymbolInfo().getType().getAlign()) {
//            case 4: //int
//            case 6://string
//            case 10: //class
//                textSegment += "\t\tmove $t1, $t0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tsne $t0, $t0, $t1\n";
//                textSegment += "\t\tmovf $t0, $zero\n";
//                break;
//            case 8: //double //TODO
//                textSegment += "\t\tmov.s $f1, $f0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tc.eq.s $f0, $f1\n";
//                textSegment += "\t\tmovt $t0, $zero\n";
//                break;
//            default:
//                throw new Exception("invalid types for equal");
//        }
//        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo()))
//            node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
//        else
//            throw new Exception("types does not match");

    }

    private void visitEqualNode(ASTNode node) throws Exception {
        LogicalOp2(node, "eq");

//        node.getChild(0).accept(this);
//        switch (node.getChild(0).getSymbolInfo().getType().getAlign()) {
//            case 4: //int
//            case 6://string
//            case 10: //class
//                textSegment += "\t\tmove $t1, $t0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tseq $t0, $t0, $t1\n";
//                break;
//            case 8: //double //TODO
//                textSegment += "\t\tmov.s $f1, $f0\n";
//                node.getChild(1).accept(this);
//                textSegment += "\t\tc.eq.s $f0, $f1\n";
//                textSegment += "\t\tmovf $t0, $zero\n";
//                break;
//
//            default:
//                throw new Exception("invalid types for equal");
//        }
//        if (isTypesEqual(node.getChild(0).getSymbolInfo(), node.getChild(1).getSymbolInfo()))
//            node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
//        else
//            throw new Exception("types does not match");
    }

    private void visitNegative(ASTNode node) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));

        textSegment += "\t\tneg " + regs.get(tempRegsNumber) + ", " + regs.get(tempRegsNumber) + "\n";

//        node.getChild();
    }

    private void visitNewArrayNode(ASTNode node) throws Exception {

        int literalNumber = ((IntegerLiteralNode) node.getChild(0).getChild(0)).getValue();

        setParentSymbolInfo(node, node.getChild(1));
        node.getSymbolInfo().setDimensionArray(node.getSymbolInfo().getDimensionArray() + 1);
        if (literalNumber <= 0)
            throw new Exception("array size must be greater than zero");
        String label = symbolTable.getCurrentScopeName() + "_NEW_ARRAY_" + arrayNumbers;
        arrayNumbers++;
        dataSegment += "\t" + label + ": .space " + (literalNumber + 1) * 4 + "\n";
        textSegment += "\t\tla $t0, " + label + "\n";
        textSegment += "\t\tli $t2, " + literalNumber + "\n";
        textSegment += "\t\tsw $t2, " + label + "\n";
    }

    private void visitLiteralNode(ASTNode node) {
        Literal literalNode = (Literal) node;
        node.setSymbolInfo(new SymbolInfo(node, literalNode.getType()));
        switch (literalNode.getType().getAlign()) {
            case 6: //string
                String str = ((StringLiteralNode) literalNode).getValue();
                String str_raw = str.substring(1, str.length() - 1);
                String label = "StringLiteral_" + stringLiterals.keySet().size() + 1;
                if (!stringLiterals.keySet().contains(str_raw)) {
                    stringLiterals.put(str_raw, label);
                    dataSegment += "\t" + label + ": .asciiz " + str + "\n";
                }
                textSegment += "\t\tla $t0, " + label + "\n";
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.STRING));
                break;
            case 1: //bool
                String bool_type = node.toString().equals("true") ? "1" : "0";
                textSegment += "\t\tli " + regs.get(tempRegsNumber) + ", " + bool_type + "\n";
                break;
            case 4: //int
                textSegment += "\t\tli " + regs.get(tempRegsNumber) + ", " + node + "\n";
                break;
            case 8: //double
                textSegment += "\t\tli.s " + fregs.get(tempfRegsNumber) + ", " + node + "\n";
                break;
        }
    }

    private void visitReturnNode(ASTNode node) throws Exception {
        ASTNode func = node.getParent().getParent().getParent().getParent();
        IdentifierNode idNode = (IdentifierNode) func.getChild(1);
        String varName = idNode.getValue();
        Function method = findFunction(varName);
        SymbolInfo returnType = func.getChild(0).getSymbolInfo();
        node.getChild(0).accept(this);
        if (!isTypesEqual(returnType, node.getChild(0).getSymbolInfo()))
            throw new Exception("Return type of " + varName + " is incorrect");

    }

    private void visitCallNode(ASTNode node) throws Exception {
        String varName;
        Function method = null;
        int argNumber = 0;
        for (ASTNode child : node.getChildren()) {
            if (child.getNodeType().equals(NodeType.IDENTIFIER)) {
                IdentifierNode idNode = (IdentifierNode) child;
                varName = idNode.getValue();
                method = findFunction(varName);
                if (method == null)
                    throw new Exception(varName + " function doesn't exist");
            }
            if (child.getNodeType().equals(NodeType.ACTUALS)) {

                for (ASTNode childChild : child.getChild(0).getChildren()) {
                    childChild.accept(this);
                    SymbolInfo si = childChild.getSymbolInfo();
                    if (!isTypesEqual(si, method.getArgumentsType().get(argNumber)))
                        throw new Exception("types doesn't match");

                    argNumber++;
                    switch (si.getType().getAlign()) {
                        case 1: //bool
                        case 4: // int
                        case 6: //String
                        case 10:
                            //TODO
                            textSegment += "\t\tsw $t0, 0($sp)\n";
                            textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                            break;
                        case 8: // float
                            textSegment += "\t\tsw $t0, 0($sp)\n";
                            textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                            break;
                        default:
                            break;
                    }
                }

            }
        }
        if (argNumber != method.getArgumentsType().size())
            throw new Exception("expected " + method.getArgumentsType().size() + " args but " + argNumber + " passed");
        textSegment += "\t\tjal " + method.getScope().getName() + "_" + method.getName() + "\n";
        textSegment += "\t\taddi $sp, $sp, " + (argNumber) * (-4) + "\n";

        node.setSymbolInfo(method.getReturnType());
    }

    private void visitReadIntegerNode(ASTNode node) {
        SymbolInfo si = new SymbolInfo(node, PrimitiveType.INT);
        node.setSymbolInfo(si);
        textSegment += "\t\tli $v0, 5\n\t\tsyscall\n";
        textSegment += "\t\tmove $t0, $v0\n\n";
    }

    private String labelGenerator() {
        return "L" + (++labelIndex);
    }

    private void visitIfStatement(ASTNode node) throws Exception {

        String ifFalseLabel = labelGenerator();
        tempRegsNumber = 8; // assigning the expStmt into register $t0
        String ifType;
        if (node.getChildren().size() == 2) {
            ifType = "if";
        } else {
            ifType = node.getChildren().size() == 3 ? "if_else" : "invalid";
        }

        //it is if statement, so next child is expStmt which is the 0 child
        node.getChild(0).accept(this);
        if (node.getChild(0).getSymbolInfo().getType().getAlign() == 1) {
            textSegment += "\t\tbne " + regs.get(tempRegsNumber) + ", 0" + ", " + ifFalseLabel + "\n";
        } else {
            throw new Exception("Invalid Expression in if_exp");
        }

        node.getChild(1).accept(this);

        textSegment += ifFalseLabel + ":\n";

        if (ifType.equals("if_else")) {

            //it is if_else stmt, so the third child must be visited
            node.getChild(2).accept(this);
        } else if (!ifType.equals("if")) {
            throw new Exception("invalid if");
        }
    }

    private void visitWhileNode(ASTNode node) throws Exception {
        String whileLabel = labelGenerator();
        String exitWhileLabel = labelGenerator();
        String continueLabel = labelGenerator();
        String breaklabel = labelGenerator();
        tempRegsNumber = 8;
        //while Exp_stmt is the first child of the while statement so,
        node.getChild(0).accept(this);
        //should check that it's an expression
        if (node.getChild(0).getSymbolInfo().getType().getAlign() == 1) {
            textSegment += whileLabel + ":";
            textSegment += "\t\tbeq " + regs.get(tempRegsNumber) + ", 0 " + exitWhileLabel + "\n";
        }
        //after checking the exp_stmt in while, we should visit while body
        node.getChild(1).accept(this);
        textSegment += "\t\tj " + whileLabel + "\n";
        textSegment += exitWhileLabel + ":";
    }

    private void visitForNode(ASTNode node) throws Exception {
        String label = labelGenerator();
        setParentSymbolInfo(node, node.getChild(0));
        textSegment += "\t\t"+label+":"+"\n";
        setParentSymbolInfo(node, node.getChild(1));
        textSegment += "\t\tbeq $t0, $zero exit"+label+"\n";
//        setParentSymbolInfo(node, node.getChild(2));
        node.getChild(2).accept(this);
        setParentSymbolInfo(node, node.getChild(3));
        textSegment += "\t\tj "+label+"\n";
        textSegment += "\t\texit"+label+":\n";
    }


    private void visitPrintNode(ASTNode node) throws Exception {
        for (ASTNode child : node.getChild(0).getChildren()) {
            child.accept(this);
            Type exprType = child.getSymbolInfo().getType();
            switch (exprType.getAlign()) {
                case 1: //bool
                    String generatedLabel = labelGenerator();
                    textSegment += "\t\tli $v0, 1\n";
                    textSegment += "\t\tbeq $t0, $zero, printFalse" + generatedLabel + "\n";
                    textSegment +=
                            "\t\tla $t0, true\n" +
                                    "\t\tli $v0, 4\n" +
                                    "\t\tadd $a0, $t0, $zero\n" +
                                    "\t\tsyscall\n" +
                                    "\t\tb endPrintFalse" + generatedLabel + "\n" +
                                    "\tprintFalse" + generatedLabel + ":\n" +
                                    "\t\tla $t0, false\n" +
                                    "\t\tli $v0, 4\n" +
                                    "\t\tadd $a0, $t0, $zero\n" +
                                    "\t\tsyscall\n" +
                                    "\tendPrintFalse" + generatedLabel + ":\n";
                    break;
                case 4: //int
                    textSegment += "\t\tli $v0, 1\n";
                    textSegment += "\t\tadd $a0, $t0, $zero\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                case 6://string
                    textSegment += "\t\tli $v0, 4\n";
                    textSegment += "\t\tadd $a0, $t0, $zero\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                case 8://float
                    textSegment += "\t\tli $v0, 2\n";
                    textSegment += "\t\tmov.d\t$f12, $f0\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                default:
                    break;
            }
        }
        textSegment += "\t\t#print new Line\n";
        textSegment += "\t\taddi $a0, $0, 0xA\n\t\taddi $v0, $0, 0xB\n\t\tsyscall \n";
    }


    private void visitClassDeclarationNode(ASTNode node) throws Exception {
        //TODO
        //
        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        String className = idNode.getValue();
        ClassDecaf.currentClass = findClass(className);
        //type
        symbolTable.enterScope(className);
        visitAllChildren(node);
        symbolTable.leaveCurrentScope();
    }

    private void visitModNode(ASTNode node) throws Exception {
        ArithmeticOp2(node, "mod");
    }

    private void visitDivisionNode(ASTNode node) throws Exception {
        ArithmeticOp2(node, "div");
    }

    private void visitMultiplicationNode(ASTNode node) throws Exception {
        ArithmeticOp2(node, "mul");
    }

    private void ArithmeticOp2(ASTNode node, String type) throws Exception {
        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo first = node.getSymbolInfo();
        int firstType = first.getType().getAlign();
        String main_type = type;
        int tempReg = firstType == 4 ? tempRegsNumber : tempfRegsNumber;
        List<String> reg = firstType == 4 ? regs : fregs;


        textSegment += "\t\tmove " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
        textSegment += "\t\tsw " + reg.get(tempReg + 1) + ", 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";

        if (firstType != 4 && type.equals("mod")) {
            throw new Exception("bad parameters for mod operation");
        } else if (type.equals("mod")) {
            main_type = "div";
        }

        if (!(firstType == 4 || firstType == 8)) {
            throw new Exception("bad parameters for type " + type);
        }

        String op = firstType == 4 ? main_type + " " : main_type + ".s ";
        String op2 = firstType == 4 ? "move " : "mov.s ";


        setParentSymbolInfo(node, node.getChild(1));
        SymbolInfo second = node.getSymbolInfo();
        String secondType = second.getType().getSignature();

        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\tlw " + reg.get(tempReg + 1) + " 0($sp)\n";


        if (isTypesEqual(first, second)) {
            textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
            if (type.equals("mod")) {
                textSegment += "\t\tmfhi $t1\n";
            }
        } else {
            throw new Exception("Type " + firstType + " & " + secondType + " are mismatched");
        }

        textSegment += "\t\t" + op2 + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
    }

    private void visitSubtractionNode(ASTNode node) throws Exception {
        ArithmeticOp1(node, "sub");
    }

    private void visitAdditionNode(ASTNode node) throws Exception {
        ArithmeticOp1(node, "add");
    }


    private void ArithmeticOp1(ASTNode node, String type) throws Exception {


        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo first = node.getSymbolInfo();
        int firstType = first.getType().getAlign();


        int tempReg = firstType == 4 ? tempRegsNumber : tempfRegsNumber;
        List<String> reg = firstType == 4 ? regs : fregs;

        textSegment += "\t\tmove " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
        textSegment += "\t\tsw " + reg.get(tempReg + 1) + ", 0($sp)\n";
        textSegment += "\t\taddi $sp, $sp, 4\n";

        if (!(firstType == 4 || firstType == 8)) {
            throw new Exception("bad parameters for this " + type);
        }


        String op = firstType == 4 ? type + " " : type + ".s ";
        String op2 = firstType == 4 ? "move " : "mov.s ";

        setParentSymbolInfo(node, node.getChild(1));
        SymbolInfo second = node.getSymbolInfo();
        String secondType = second.getType().getSignature();

        textSegment += "\t\taddi $sp, $sp, -4\n";
        textSegment += "\t\tlw " + reg.get(tempReg + 1) + " 0($sp)\n";

        if (isTypesEqual(first, second)) {
            textSegment += "\t\t" + op + reg.get(tempReg + 1) + ", " + reg.get(tempReg + 1) + ", " + reg.get(tempReg) + "\n";
        } else {
            throw new Exception("Type " + firstType + " & " + secondType + " are mismatched");
        }
        textSegment += "\t\t" + op2 + reg.get(tempReg) + ", " + reg.get(tempReg + 1) + "\n";
    }

    private void visitLValueNode(ASTNode node) throws Exception {
        node.getChild(0).accept(this);
        if (node.getChildren().size() == 1) { //id
            IdentifierNode idNode = (IdentifierNode) node.getChild(0);
            String varName = idNode.getValue();
            SymbolInfo varType = (SymbolInfo) symbolTable.get(varName);
            SymbolInfo si = new SymbolInfo(node, varType.getType());
            si.setDimensionArray(varType.getDimensionArray());
            node.setSymbolInfo(si);
            switch (varType.getType().getAlign()) {
                case 1: //bool
                case 4: // int
                case 6: //String
                    textSegment += "\t\tla $a0, " + findNameOfId(varName) + '\n';
                    textSegment += "\t\tlw $t0, 0($a0)\n";
                    break;
                case 8: // float
                    textSegment += "\t\tla $a0, " + findNameOfId(varName) + '\n';
                    textSegment += "\t\tl.s $f0, 0($a0)\n";
                    break;
                //todo
                default:
                    break;
            }
        } else {
            if (node.getChild(1).getNodeType().equals(NodeType.IDENTIFIER)) {
                //TODO
            } else {
                node.getChild(0).accept(this);
                textSegment += "\t\tmove $a3, $t0\n";
                textSegment += "\t\tmove $s4, $a0\n";
                SymbolInfo varType = node.getChild(0).getSymbolInfo();
                node.getChild(1).accept(this);
                SymbolInfo varType2 = node.getChild(1).getSymbolInfo();
                System.out.println(varType2.getDimensionArray());
                if (varType2.getType().getAlign() == 4) {//int
                    if (varType.getDimensionArray() > 0) {
                        textSegment += "\t\tli $t4, 4\n";
                        textSegment += "\t\tlw $t2, 0($a3)\n";
                        textSegment += "\t\tblt $t2, $t0, runtime_error\n";
                        textSegment += "\t\tbeq $t2, $t0, runtime_error\n";
                        textSegment += "\t\tmul $t0, $t0, $t4\n";
                        textSegment += "\t\tadd $a0, $s4, $t0\n";
                        textSegment += "\t\tlw $t0, 0($a0)\n";
                    } else
                        throw new Exception("error in array assign - type is not array");
                } else
                    throw new Exception("error in array assign - index array");

                SymbolInfo si = new SymbolInfo(node, varType.getType());
                si.setDimensionArray(varType.getDimensionArray() - 1);
                node.setSymbolInfo(si);
            }
        }

    }


    private void visitAssignNode(ASTNode node) throws Exception { // age kharab shod az commit Better Addition drst kn
        setParentSymbolInfo(node, node.getChild(0));
        SymbolInfo varType = node.getChild(0).getSymbolInfo();
        textSegment += "\t\tla $a3, 0($a0) \n";
        node.getChild(1).accept(this);
        SymbolInfo exprType = node.getChild(1).getSymbolInfo();
        if (exprType == null)
            throw new Exception("Assign Error");
        //TODO
        if (isTypesEqual(varType, exprType)) {
            switch (varType.getType().getAlign()) {
                case 6: //string
                case 1: //bool
                case 4: // int
                    textSegment += "\t\tsw $t0, 0($a3)\n";
                    break;
                case 8: // float
                    textSegment += "\t\ts.s $f0, 0($a3)\n";
                    break;
                case 10:
                    //todo
                    break;
                default:
                    break;
            }
        } else {
            throw new Exception("Type " + varType + " & " + exprType + " Doesnt Match");
        }
    }

    private void visitExpressionNode(ASTNode node) throws Exception {
        tempRegsNumber = 8;

//        textSegment += "\t\tmove $s0, $t1\n";
        setParentSymbolInfo(node, node.getChild(0));
//        textSegment += "\t\tmove $t1, $s0\n";


//        visitAllChildren(node);
    }

    private void visitStatementNode(ASTNode node) throws Exception {
        visitAllChildren(node);
//        setParentSymbolInfo(node, node.getChild(0));

    }

    private void visitStatementsNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitArgumentsNode(ASTNode node) throws Exception {
        int argumentsLen = node.getChildren().size() * (-4);
        Function function = Function.currentFunction;
        if (argumentsLen < 0)
            textSegment += "\t\taddi $sp,$sp," + argumentsLen + "\n";
        for (int i = argumentsLen / (-4); i >= 1; i--) {
            ASTNode ArgumentNode = node.getChild(i - 1);
            ArgumentNode.getChild(0).accept(this);
            IdentifierNode idNode = (IdentifierNode) ArgumentNode.getChild(0).getChild(1);
            String idName = idNode.getValue();
            SymbolInfo si = function.getArgumentsType().get(i - 1);
            switch (si.getType().getAlign()) {
                case 1: //bool
                case 4: // int
                case 6: //String
                case 10:
                    //TODO
                    textSegment += "\t\tla $a1, " + findNameOfId(idName) + '\n';
                    textSegment += "\t\tlw $t1, 0($sp)\n";
                    textSegment += "\t\tsw $t1, 0($a1)\n";
                    textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                    break;
                case 8: // float
                    textSegment += "\t\tla $a1, " + findNameOfId(idName) + '\n';
                    textSegment += "\t\tl.s $f1, 0($sp)\n";
                    textSegment += "\t\ts.s $f1, 0($a1)\n";
                    textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                    break;
                default:
                    break;
            }
        }

    }


    private void visitStartNode(ASTNode node) throws Exception {
        dataSegment += "\terror: .asciiz \"runtime ERROR\"\n";
        textSegment += ".text\n" + "\t.globl main\n\n";
        textSegment += "\tmain:\n";
        textSegment += "\t\tjal global_main\n";

        textSegment += "\t\t#END OF PROGRAM\n";
        textSegment += "\t\tli $v0,10\n\t\tsyscall\n";

        textSegment += "\truntime_error:\n";
        textSegment += "\t\tli $v0, 4\n";
        textSegment += "\t\tla $a0, error\n";
        textSegment += "\t\tsyscall\n";

        textSegment += "\t\t#END OF PROGRAM\n";
        textSegment += "\t\tli $v0,10\n\t\tsyscall\n";

        symbolTable.enterScope("global");
        visitAllChildren(node);
        stream.print(dataSegment + '\n' + textSegment);
    }


    private void visitBlockNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitVariableDeclaration(ASTNode node) throws Exception {
        if (ClassDecaf.currentClass == null || !symbolTable.getCurrentScopeName().equals(ClassDecaf.currentClass.getName())) {
            IdentifierNode idNode = (IdentifierNode) node.getChild(1);
            String varName = idNode.getValue();
            String label = symbolTable.getCurrentScopeName() + "_" + varName + " :";
            setParentSymbolInfo(node, node.getChild(0));
            int dimensionArray = node.getSymbolInfo().getDimensionArray();
            if (!node.getChild(0).getNodeType().equals(NodeType.IDENTIFIER)) {
                Type typePrimitive = node.getSymbolInfo().getType();
                if (dimensionArray == 0 && !typePrimitive.getSignature().equals(".ascii"))
                    dataSegment += "\t" + label + " " + typePrimitive.getSignature() + " " + typePrimitive.getPrimitive().getInitialValue() + "\n";
                else
                    dataSegment += "\t" + label + " .word 0" + "\n";
            } else {
                IdentifierNode typeNode = (IdentifierNode) node.getChild(0);
                String typeName = typeNode.getValue();
                ClassDecaf classDecaf = findClass(typeName);
                if (classDecaf == null)
                    throw new Exception(typeName + " class not Declared");
                dataSegment += "\t" + label + "\t" + ".space" + "\t" + classDecaf.getObjectSize() + "\n";
            }
            symbolTable.put(varName, node.getSymbolInfo());
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
        node.getChild(0).accept(this);
        SymbolInfo returnType = node.getChild(0).getSymbolInfo();

        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();
        Function method_temp = new Function(methodName, returnType, symbolTable.getCurrentScope());
        for (Function function : functions) {
            if (function.equals(method_temp)) {
                Function.currentFunction = function;
                System.out.println(function);
                break;
            }
        }
        String label = symbolTable.getCurrentScopeName() + "_" + methodName;
        textSegment += "\t" + label + ":\n";
        symbolTable.enterScope(label);
        textSegment += "\t\tsw $ra,0($sp)\n";
        node.getChild(2).accept(this);
        textSegment += "\t\taddi $sp,$sp,4\n";
        node.getChild(3).accept(this);
        textSegment += "\t\taddi $sp,$sp,-4\n";
        textSegment += "\t\tlw $ra,0($sp)\n";
        textSegment += "\t\tjr $ra\n";

        symbolTable.leaveCurrentScope();
    }


    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    private String getBlock() {
        return "" + blockIndex++;
    }

    private boolean isTypesEqual(SymbolInfo a, SymbolInfo b) {
        if (a.getType().getAlign() == b.getType().getAlign()) {
            if (a.getType().getSignature().equals(b.getType().getSignature())) {
                if (a.getDimensionArray() == b.getDimensionArray())
                    return a.getType().getPrimitive().equals(b.getType().getPrimitive());
            }
        }
        return false;
    }

    private String findNameOfId(String id) {
        return symbolTable.getScopeNameOfIdentifier(id) + "_" + id;
    }

    private Function findFunction(String varName) {
        Function method = null;
        for (Function function : functions) {
            if (function.getName().equals(varName)) {
                for (Scope scope : symbolTable.getScopes()) {
                    if (scope.equals(function.getScope())) {
                        method = function;
                        break;
                    }
                }
                if (method != null)
                    break;
            }
        }
        return method;
    }

    private void setParentSymbolInfo(ASTNode node, ASTNode child) throws Exception {
        child.accept(this);
//        System.err.print("Node is " + node + " Child is " + child);
        Type type = child.getSymbolInfo().getType();
        SymbolInfo si = new SymbolInfo(node, type);
//        System.err.println(" child type:" + type);
        si.setDimensionArray(child.getSymbolInfo().getDimensionArray());
        node.setSymbolInfo(si);
    }

    private ClassDecaf findClass(String name) {
        for (ClassDecaf aClass : classes) {
            if (aClass.getName().equals(name))
                return aClass;
        }
        return null;
    }

}

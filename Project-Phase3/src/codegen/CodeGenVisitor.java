package codegen;

import AST.*;


import java.io.PrintStream;
import java.util.ArrayList;
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
    private List<Function> functions = new ArrayList<>();

    private SymbolTable symbolTable = new SymbolTable();
    private int blockIndex;


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
            case NEGATIVE:
                break;
            case READ_INTEGER:
                visitReadIntegerNode(node);
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
                visitCallNode(node);
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
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.BOOL));
                break;
            case DOUBLE_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.DOUBLE));
                break;
            case INT_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.INT));
                break;
            case STRING_TYPE:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.STRING));
                break;
            case VOID:
                node.setSymbolInfo(new SymbolInfo(node, PrimitiveType.VOID));
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
                visitClassDeclarationNode(node);
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
                break;
            case WHILE_STATEMENT:
                break;
            case PRINT_STATEMENT:
                visitPrintNode(node);
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

    private void visitReturnNode(ASTNode node) throws Exception {
        ASTNode func = node.getParent().getParent().getParent().getParent();
        IdentifierNode idNode = (IdentifierNode) func.getChild(1);
        String varName = idNode.getValue();
        Function method = findFunction(varName);
        SymbolInfo returnType = func.getChild(0).getSymbolInfo();
        node.getChild(0).accept(this);
        System.out.println(returnType);
        if (isTypesEqual(returnType, node.getChild(0).getSymbolInfo())) {
            System.out.println("yes");
        } else
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
                    throw new Exception("this function doesn't exist");
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
                        case 10:
                            //TODO
                            textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                            textSegment += "\t\tsw $t0, 0($sp)\n";
                            break;
                        case 8: // float
                            textSegment += "\t\taddi $sp, $sp, " + 4 + "\n";
                            textSegment += "\t\tsw $t0, 0($sp)\n";
                            break;
                        default:
                            break;
                    }
                }

            }
        }
        if (argNumber != method.getArgumentsType().size())
            throw new Exception("expected " + method.getArgumentsType().size() + " args but " + argNumber + " passed");
        textSegment += "\t\taddi $sp, $sp, 4\n";
        textSegment += "\t\tjal " + method.getScope().getName() + "_" + method.getName() + "\n";
        textSegment += "\t\taddi $sp, $sp, " + (argNumber + 1) * (-4) + "\n";

        node.setSymbolInfo(method.returnType);
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

        String ifTrueLabel = labelGenerator();
        String ifFalseLabel = labelGenerator();

        tempRegsNumber = 8; // assigning the expStmt into register $t0
        String ifType;
        if (node.getChildren().size() == 2) {
            ifType = "if";
        } else {
            ifType = node.getChildren().size() == 3 ? "if_else" : "invalid";
        }


        if (ifType.equals("if")) {
            //it is if statement, so next child is expStmt which is the 0 child
            node.getChild(0).accept(this);
            visitExpressionNode(node);

            textSegment += "\t\tbeq " + regs.get(tempRegsNumber) + ", 0, " + ifTrueLabel + "\n";
            textSegment += ifTrueLabel + ":\n";

            node.getChild(1).accept(this);
            visitStatementNode(node);


        } else if (ifType.equals("if_else")) {

            textSegment += "\t\tbeq " + regs.get(tempRegsNumber) + ", 0," + ifFalseLabel + "\n";
            textSegment += ifFalseLabel + "\n";

            //it is if_else stmt, so the third child must be visited
            node.getChild(2).accept(this);
            visitStatementNode(node);
        } else {
            System.out.println("***ERROR - INVALID IF***");
        }


//        textSegment += "\t\tbeq " + regs.get(tempRegsNumber) + ", 0  " + ifTrueLabel + "\n";
//        textSegment += ifTrueLabel + ":\n";

    }


    private void visitPrintNode(ASTNode node) throws Exception {
        for (ASTNode child : node.getChild(0).getChildren()) {
            child.accept(this);
            Type exprType = child.getSymbolInfo().getType();
            switch (exprType.getAlign()) {
                case 1: //bool
                    // TODO: 1/26/21
                    break;
                case 4: //int
                    textSegment += "\t\tli $v0, 1\n";
                    textSegment += "\t\tadd $a0, $t0, $zero\n";
                    textSegment += "\t\tsyscall\n";
                    break;
                case 8://float
                    textSegment += "\t\tli $v0, 3\n";
                    textSegment += "\t\tmov.s\t$f12, $f0\n";
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

        //type
        symbolTable.enterScope(symbolTable.getCurrentScopeName() + "_" + className);
        for (ASTNode child : node.getChildren()) {
            if (child.getNodeType().equals(NodeType.FIELDS)) {
                child.accept(this);
            }
        }
        symbolTable.leaveScopeType(symbolTable.getCurrentScopeName() + "_" + className);
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
        node.getChild(0).accept(this);
        IdentifierNode idNode = (IdentifierNode) node.getChild(0);
        String varName = idNode.getValue();
        SymbolInfo varType = (SymbolInfo) symbolTable.get(varName);
        node.setSymbolInfo(varType);

        switch (varType.getType().getAlign()) {
            case 1: //bool
            case 4: // int
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

    }

    private void visitAssignNode(ASTNode node) throws Exception {
        IdentifierNode idNode = (IdentifierNode) node.getChild(0).getChild(0);
        String varName = idNode.getValue();
        node.getChild(1).accept(this);
        SymbolInfo varType = (SymbolInfo) symbolTable.get(varName);
        SymbolInfo exprType = node.getChild(1).getSymbolInfo();
        if (exprType == null)
            throw new Exception("Assign Error");
        //TODO
        if (isTypesEqual(varType, exprType)) {

            switch (varType.getType().getAlign()) {
                case 1: //bool
                case 4: // int
                    textSegment += "\t\tla $a0, " + findNameOfId(varName) + '\n';
                    textSegment += "\t\tsw $t0, 0($a0)\n";
                    break;
                case 8: // float
                    textSegment += "\t\tla $a0, " + findNameOfId(varName) + '\n';
                    textSegment += "\t\ts.s $f0, 0($a0)\n";
                    break;
                case 10:
                    //todo
                    break;
                default:
                    break;
            }
        } else {
            throw new Exception("Type " + varName + " Doesnt Match");
        }

    }

    private void visitExpressionNode(ASTNode node) throws Exception {
        tempRegsNumber = 8;
        visitAllChildren(node);

        node.setSymbolInfo(node.getChild(0).getSymbolInfo());

    }

    private void visitStatementNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitStatementsNode(ASTNode node) throws Exception {
        visitAllChildren(node);
    }

    private void visitArgumentsNode(ASTNode node) throws Exception {
        textSegment += "\t\tsw $ra,0($sp)\n";
        int argumentsLen = node.getChildren().size() * (-4);
        Function function = functions.get(functions.size() - 1); //shak drm be in
        if (argumentsLen < 0)
            textSegment += "\t\taddi $sp,$sp," + argumentsLen + "\n";
        for (int i = argumentsLen / (-4); i >= 1; i--) {
            ASTNode ArgumentNode = node.getChild(i - 1);
            ArgumentNode.getChild(0).accept(this);
            IdentifierNode idNode = (IdentifierNode) ArgumentNode.getChild(0).getChild(1);
            String idName = idNode.getValue();
            SymbolInfo si = (SymbolInfo) symbolTable.get(idName);
            function.getArgumentsType().add(si);
            switch (si.getType().getAlign()) {
                case 1: //bool
                case 4: // int
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
        textSegment += ".text\n" + "\t.globl main\n\n";
        textSegment += "\tmain:\n";
        textSegment += "\t\tjal global_main\n";
        textSegment += "\t\t#END OF PROGRAM\n";
        textSegment += "\t\tli $v0,10\n\t\tsyscall\n";
        symbolTable.enterScope("global");
        visitAllChildren(node);
        stream.print(dataSegment + '\n' + textSegment);
    }


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
        String label = symbolTable.getCurrentScopeName() + "_" + varName + " :";

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
            SymbolInfo si = new SymbolInfo(idNode, PrimitiveType.INT);
            si.setType(typePrimitive);
            idNode.setSymbolInfo(si);
            symbolTable.put(varName, si);
        } else {
            IdentifierNode typeIdNode = (IdentifierNode) node.getChild(0);
            String idName = typeIdNode.getValue();
            IdentifierType identifierType = new IdentifierType(idName);

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
        node.getChild(0).accept(this);
        SymbolInfo returnType = node.getChild(0).getSymbolInfo();

        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();
        Function method = new Function(methodName, returnType, symbolTable.getCurrentScope());
        if (functions.contains(method)) {
            throw new Exception("this function declared before");
        }
        functions.add(method);
        String label = symbolTable.getCurrentScopeName() + "_" + methodName;
        if (symbolTable.getCurrentScopeName().equals("global") && methodName.equals("main"))
            textSegment += "\t" + label + ":\n";
        else
            textSegment += "\t" + label + ":\n";


        symbolTable.enterScope(label);


        node.getChild(2).accept(this);
        node.getChild(3).accept(this);
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
}
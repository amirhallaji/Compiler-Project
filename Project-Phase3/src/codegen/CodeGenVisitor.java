package codegen;

import AST.*;
import codegen.SimpleVisitor;

import java.io.PrintStream;
import java.util.*;

import AST.ClassNode;
import semantic.SymbolInfo;


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

    private static String dataSegment = ".data \n";
    private static String textSegment = ".text \n" + "      .globl main\n";

    static Map<String, HashSet<Signature>> signatures = new HashMap<>();
    private SymbolTable symbolTable = new SymbolTable();
    private int blockIndex;

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
                visitAllChildren(node);
                //TODO
                break;
            case LOCAL_VAR_DECLARATION:
                break;
            case VARIABLE_DECLARATION:
                break;
            case VARIABLE_CONST_DECLARATION:
                break;
            case METHOD_DECLARATION:
                visitMethodDeclarationNode(node);
                //DONE
                break;
            case Class_DECLARATION:
                visitAllChildren(node);
                break;
            case DECLARATIONS:
                break;
            case ASSIGN:
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
                break;
            case STATEMENTS:
                break;
            case EXPRESSION_STATEMENT:
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
                visitArgumentNode(node);
                break;
            case ARGUMENTS:
                visitAllChildren(node);
                //DONE
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
                visitAllChildren(node);
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

    private void visitMethodDeclarationNode(ASTNode node) throws Exception {
        //type
        symbolTable.enterScopeType(getBlock());
        TypeNode returnType = (TypeNode) node.getChild(0);
        String returnSig = returnType.getType().getSignature();
        //identifier
        IdentifierNode idNode = (IdentifierNode) node.getChild(1);
        String methodName = idNode.getValue();
        String label;
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
        symbolTable.leaveScopeType(blockIndex - 1 + "");
    }

    private void visitArgumentNode(ASTNode node) throws Exception {

    }

    private void visitAllChildren(ASTNode node) throws Exception {
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    private String getBlock() {
        return "" + blockIndex++;
    }
}
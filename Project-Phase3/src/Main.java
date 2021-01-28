import java.io.File;

import AST.ASTNode;
import AST.Program;
import Scanner.Scanner;
import codegen.CodeGenVisitor;

import java.io.*;

import AST.ASTNode;
import AST.Program;
//import codegen.CodeGenVisitor;
import Parser.Parser;
import Scanner.Scanner;
import codegen.CodeGenVisitor;
//import codegen.MethodVisitor;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            String inputFile = null;
            String outputFile = null;
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-i")) {
                        inputFile = args[i + 1];
                    }
                    if (args[i].equals("-o")) {
                        outputFile = args[i + 1];
                    }
                }
            }
            PrintStream writer = null;
            if (outputFile != null) {
                writer = new PrintStream(outputFile);
            }
            Compiler_test compiler = new Compiler_test(inputFile, writer);
            compiler.run();

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            PrintStream writer;
            String outputFile = null;
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-o")) {
                        outputFile = args[i + 1];
                    }
                }
            }
            if (outputFile != null) {
                writer = new PrintStream("out/" + outputFile);
            } else {
                writer = new PrintStream(System.out);
            }

            writer.flush();
            writer.close();
        }
    }

}


class Compiler_test {
    private String source;
    private PrintStream writer;

    Compiler_test(String source, PrintStream writer) {
        this.source = source;
        this.writer = writer;
    }

    public void run() {
        processFile();
    }

    private void printTree(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            System.out.println(child + " ---------> " + node);
        }
        for (ASTNode child : node.getChildren()) {
            printTree(child);
        }

    }

    private void processFile() {
        Program cu;
        try {
            cu = parse();
        } catch (Exception e) {
            writer.print("parser exception");
            return;
        }

//        performSemanticAnalysis(cu);
        try {
            generateCode(cu);
        } catch (Exception e) {
            writer.print("code gen exception");
        }
    }


    private Program parse() throws Exception {
        System.out.println("parsing");
        FileInputStream inStream = new FileInputStream(source);
        DataInputStream distress = new DataInputStream(new BufferedInputStream(inStream));

        Parser parser = new Parser(new Scanner(distress));
        parser.parse();
//        System.out.println("--------------");
//        printTree(parser.getRoot());
//        System.out.println("--------------");

        System.out.println("parsing done\n");
        return parser.getRoot();
    }

    //    private void performSemanticAnalysis(Program cu) throws Exception {
//        System.out.println("in type visitor");
//        cu.accept(new MethodVisitor());
//        System.out.println("TV done\n");
//    }
//
    private void generateCode(Program cu) throws Exception {
        System.out.println("in code gen");
        cu.accept(new CodeGenVisitor(writer));
        System.out.println("CG done\n");
    }
}

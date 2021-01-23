import AST.Program;
//import codegen.CodeGenVisitor;
import Parser.Parser;
import Scanner.Scanner;
//import codegen.MethodVisitor;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws Exception {
        System.out.println(('a' + 1));
        String source = "src/tests/1.txt";

        Compiler compiler = new Compiler(source);
        compiler.run();
    }

    private String source;

    private Compiler(String source) {
        this.source = source;
    }

    private void run() throws Exception {
        processFile();
    }

    private void processFile() throws Exception {
        PrintStream stream = new PrintStream(new FileOutputStream("src/tests/Result.ll"));
//        PrintStream stream = System.out;
        Program cu = parse();
//        performSemanticAnalysis(cu);
//        generateCode(cu, stream);
    }

    private Program parse() throws Exception {
        System.out.println("parsing");
        FileInputStream inStream = new FileInputStream(source);
        DataInputStream distress = new DataInputStream(new BufferedInputStream(inStream));

        Parser parser = new Parser(new Scanner(distress));
        parser.parse();
        System.out.println("--------------");
        System.out.println(parser.getRoot());
        System.out.println(parser.getRoot().getChildren());
        System.out.println(parser.getRoot().getChildren().get(0).getChildren());
        System.out.println("--------------");

        System.out.println("parsing done\n");
        return parser.getRoot();
    }

//    private void performSemanticAnalysis(Program cu) throws Exception {
//        System.out.println("in type visitor");
//        cu.accept(new MethodVisitor());
//        System.out.println("TV done\n");
//    }
//
//    private void generateCode(Program cu, PrintStream stream) throws Exception {
//        System.out.println("in code gen");
//        cu.accept(new CodeGenVisitor(stream));
//        stream.close();
//        System.out.println("CG done\n");
//    }
}

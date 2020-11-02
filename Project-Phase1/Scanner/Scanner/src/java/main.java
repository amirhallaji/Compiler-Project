import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        Laxer scanner = new Laxer(new FileReader("./tests/" + args[1]));
        FileWriter writer = new FileWriter("./out/" + args[3]);
        while (true) {
            int a = scanner.yylex();
            if (a == Laxer.YYEOF) {
                System.out.println("out:\n" + scanner.out.toString());
                writer.write(scanner.out.toString());
                writer.close();
                break;
            }
        }
    }
}
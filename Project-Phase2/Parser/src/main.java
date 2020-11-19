import java.io.FileNotFoundException;
import java.io.FileReader;

public class main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader("src/tests/t05.in");
        Laxer laxer = new Laxer(fileReader);
        parser parser = new parser(laxer);
        System.err.close();
        try {
            parser.parse();
            System.out.println("OK");
        } catch (Exception e) {
            System.out.println("Syntax error");
        }

    }
}



import java.io.FileNotFoundException;
import java.io.FileReader;

public class main {
    public static void main(String[] args) throws Exception {
        FileReader fileReader = new FileReader("src/Tests/first.txt");
        Laxer laxer = new Laxer(fileReader);
        parser parser = new parser(laxer);
        parser.parse();
        System.out.println("OK");
    }
}

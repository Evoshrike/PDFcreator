import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PDFCreator {
    private final String filepath;


    public PDFCreator(String filepath) {
        this.filepath = filepath;
    }

    public void read(){
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));
            for (String line : lines) {
                System.out.println(line);
            }
            List<LineContent> lineContents = Parser.parse(lines);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

    }

}

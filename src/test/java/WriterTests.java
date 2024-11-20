import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriterTests {
    static Document document;
    @BeforeEach
    void initialiseDocument()  {
        // Create document for each test to write to. Stored as field so accessible by all tests.
        PdfWriter pdfWriter;
        try {
            pdfWriter = new PdfWriter("unitTestOutput.pdf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not create pdf file for unit test");
        }
        PdfDocument pdf = new PdfDocument(pdfWriter);
        document = new Document(pdf);

    }

    @AfterEach
    void closeDocument(){
        document.close();
    }
    @Test
    void writerSetsMarginLeft(){
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.INDENT, 2));
        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        float indentAfter = w.getParagraph().getMarginLeft().getValue();


        // Assert
        assertEquals( w.getIndentAmt()*2, indentAfter);
        assertEquals(w.getIndentNumber(), 2);
    }

    @Test
    void writerWritesText(){
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new TextLine("Hello World"));
        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        Text t = getParagraphText(w.getParagraph()).get(0);

        // Assert
        assertEquals("Hello World", t.getText());



    }

    @Test
    void writerSetsFill(){
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.FILL));
        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        TextAlignment textAlignment = w.getParagraph().getProperty(Property.TEXT_ALIGNMENT);

        // Assert
        assertEquals(TextAlignment.JUSTIFIED, textAlignment);

    }

    @Test
    void writerSetsNoFill(){
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.NO_FILL));
        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        TextAlignment textAlignment = w.getParagraph().getProperty(Property.TEXT_ALIGNMENT);

        // Assert
        assertEquals(TextAlignment.LEFT, textAlignment);

    }
    @Test
    void writerCreatesNewParagraph() {
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new TextLine("Hello World"));
        lines.add(new FunctionLine(Function.PARAGRAPH));
        lines.add(new TextLine("Line 2"));
        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        LinkedList<Text> text = getParagraphText(w.getParagraph());

        // Assert
        assertEquals(1, text.size());
        assertEquals("Line 2", text.get(0).getText());



    }

    @Test
    void writerMakesTextLarge(){
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.LARGE));
        lines.add(new TextLine("Hello World"));

        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        Text text = getParagraphText(w.getParagraph()).get(0);
        UnitValue fontSize = text.getProperty(Property.FONT_SIZE);

        // Assert
        assertEquals(32.0F, fontSize.getValue());
    }

    @Test
    void writerMakesTextSmall(){
        // Arrange line content, initialise writer.
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.LARGE));
        lines.add(new TextLine("Hello World"));
        lines.add(new FunctionLine(Function.NORMAL));
        lines.add(new TextLine("Line 2"));
        Writer w = new Writer(lines, document);

        // Write + get relevant data
        w.write();
        Text text = getParagraphText(w.getParagraph()).get(1);
        UnitValue fontSize = text.getProperty(Property.FONT_SIZE);

        // Assert
        assertEquals(12.0F, fontSize.getValue());
    }

    // Supplementary method to get paragraph text to avoid verbosity in tests
    public static LinkedList<Text> getParagraphText(Paragraph paragraph) {
        LinkedList<Text> texts = new LinkedList<>();
        List<com.itextpdf.layout.element.IElement> children = paragraph.getChildren();
        for (com.itextpdf.layout.element.IElement child : children) {
            if (child instanceof Text) {
                texts.add((Text) child);
            }
        }
        return texts;
    }
}

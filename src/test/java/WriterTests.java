import com.itextpdf.io.font.constants.FontStyles;
import com.itextpdf.io.font.constants.FontWeights;
import com.itextpdf.kernel.font.PdfFont;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class WriterTests {
    static Document document;
    @BeforeEach
    void initialiseDocument()  {
        PdfWriter pdfWriter = null;
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
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.INDENT, 2));
        Writer w = new Writer(lines, document);


        w.write();
        float indentAfter = w.getParagraph().getMarginLeft().getValue();



        assertEquals( 30.0F, indentAfter);
        assertEquals(w.getIndentAmt(), 2);
    }

    @Test
    void writerWritesText(){
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new TextLine("Hello World"));
        Writer w = new Writer(lines, document);

        w.write();
        Text t = getParagraphText(w.getParagraph()).get(0);

        assertEquals("Hello World", t.getText());



    }

    @Test
    void writerSetsFill(){
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.FILL));
        Writer w = new Writer(lines, document);

        w.write();
        TextAlignment textAlignment = w.getParagraph().getProperty(Property.TEXT_ALIGNMENT);

        assertEquals(TextAlignment.JUSTIFIED, textAlignment);

    }

    @Test
    void writerSetsNoFill(){
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.NOFILL));
        Writer w = new Writer(lines, document);

        w.write();
        TextAlignment textAlignment = w.getParagraph().getProperty(Property.TEXT_ALIGNMENT);

        assertEquals(TextAlignment.LEFT, textAlignment);

    }
    @Test
    void writerCreatesNewParagraph() {
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new TextLine("Hello World"));
        lines.add(new FunctionLine(Function.PARAGRAPH));
        lines.add(new TextLine("Line 2"));
        Writer w = new Writer(lines, document);

        w.write();
        LinkedList<Text> text = getParagraphText(w.getParagraph());

        assertEquals(1, text.size());
        assertEquals("Line 2", text.get(0).getText());



    }

    @Test
    void writerMakesTextLarge(){
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.LARGE));
        lines.add(new TextLine("Hello World"));

        Writer w = new Writer(lines, document);

        w.write();
        Text text = getParagraphText(w.getParagraph()).get(0);
        UnitValue fontSize = text.getProperty(Property.FONT_SIZE);

        assertEquals(32.0F, fontSize.getValue());
    }

    @Test
    void writerMakesTextSmall(){
        LinkedList<LineContent> lines = new LinkedList<>();
        lines.add(new FunctionLine(Function.LARGE));
        lines.add(new TextLine("Hello World"));
        lines.add(new FunctionLine(Function.NORMAL));
        lines.add(new TextLine("Line 2"));
        Writer w = new Writer(lines, document);

        w.write();
        Text text = getParagraphText(w.getParagraph()).get(1);
        UnitValue fontSize = text.getProperty(Property.FONT_SIZE);

        assertEquals(12.0F, fontSize.getValue());
    }

    public static LinkedList<Text> getParagraphText(Paragraph paragraph) {
        LinkedList<Text> texts = new LinkedList<>();

        // Iterate through child elements of the Paragraph
        List<com.itextpdf.layout.element.IElement> children = paragraph.getChildren();
        for (com.itextpdf.layout.element.IElement child : children) {
            if (child instanceof Text) {
                texts.add((Text) child);
            }
        }
        return texts;
    }
}

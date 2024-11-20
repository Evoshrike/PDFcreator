

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.IOException;
import java.util.List;

public class Writer {
    private boolean bold;
    private boolean italic;
    private boolean largeText;
    private int indentNumber;

    private final float indentAmt;

    private Paragraph paragraph;

    private final Document document;
    private final List<LineContent> lineContents;



    public Writer(List<LineContent> lineContents, Document document) {
        this.lineContents = lineContents;
        this.document = document;
        paragraph = new Paragraph();
        bold = false;
        italic = false;
        largeText = false;
        indentNumber = 0;
        // Hardcoded here but can be changed - no reason it has to be 15.
        indentAmt = 15.0F;

    }





    public void write() {
        try {
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);


            for (LineContent c : lineContents) {
                if (c instanceof FunctionLine) {
                    handleCommand((FunctionLine) c);

                } else {
                    assert c instanceof TextLine;
                    // Create Text object for each line of input to allow bold text with normal in same line of output
                    Text text = new Text(((TextLine) c).getText());
                    text.setFont(font);
                    if (bold) text.setBold();
                    if (italic) text.setItalic();
                    if (largeText) {
                        text.setFontSize(32.0F);
                    } else {
                        text.setFontSize(12.0F);
                    }
                    paragraph.add(text);

                }
            }
            // Add last paragraph
            document.add(paragraph);
            document.close();
        } catch (IOException e) {
            document.close();
            // This should never happen (argument to createFont always legal) hence not gracefully handled
            throw new RuntimeException("Error creating fonts");
        }

    }
    // Supplementary method for write()
    private void handleCommand(FunctionLine c) {
        switch (c.getF()) {
            case BOLD: {

                bold = true;
                break;
            }
            case ITALIC: {

                italic = true;
                break;
            }
            case REGULAR: {

                italic = false;
                bold = false;
                break;
            }
            case LARGE: {
                largeText = true;
                break;
            }
            case NORMAL: {
                largeText = false;
                break;
            }
            case INDENT: {


                indentNumber += c.getIndentNumber();

                float indent = ((float) indentNumber)*indentAmt;
                paragraph.setMarginLeft((indent));
                break;
            }
            case FILL: {
                paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
                break;
            }
            case NO_FILL: {
                // ASSSUMED Nofill is default for ending a paragraph and going back to default (i.e. left aligned)
                document.add(paragraph);
                paragraph = new Paragraph();

                paragraph.setTextAlignment(TextAlignment.LEFT);
                break;
            }
            case PARAGRAPH: {
                document.add(paragraph);
                paragraph = new Paragraph();
                paragraph.setMarginLeft(((float) indentNumber)*15.0F);

            }
        }
    }

    public float getIndentAmt() {
        return indentAmt;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public int getIndentNumber() {
        return indentNumber;
    }
}

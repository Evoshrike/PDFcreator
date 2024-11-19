

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Writer {
    private boolean bold;
    private boolean italic;
    private boolean largeText;
    private int indentAmt;

    private Paragraph paragraph;

    private Document document;
    private List<LineContent> lineContents;



    public Writer(List<LineContent> lineContents, Document document) {
        this.lineContents = lineContents;
        this.document = document;
        paragraph = new Paragraph();
        bold = false;
        italic = false;
        largeText = false;
        indentAmt = 0;

    }





    public void write() {
        try {
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);


            for (LineContent c : lineContents) {
                if (c instanceof FunctionLine) {
                    handleCommand((FunctionLine) c);

                } else {
                    assert c instanceof TextLine;
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
        document.add(paragraph);
        document.close();
        } catch (IOException e) {
            throw new RuntimeException("Error creating fonts");
        }

    }

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

                // ASSUMPTION: indent is provided as int.
                indentAmt += c.getIndentAmt();
                float indent = ((float) indentAmt)*15.0F;
                paragraph.setMarginLeft((indent));
                break;
            }
            case FILL: {
                paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
                break;
            }
            case NOFILL: {
                // ASSSUMED Nofill is default for ending a paragraph and going back to default
                document.add(paragraph);
                paragraph = new Paragraph();

                paragraph.setTextAlignment(TextAlignment.LEFT);
                break;
            }
            case PARAGRAPH: {
                document.add(paragraph);
                paragraph = new Paragraph();
                paragraph.setMarginLeft(((float) indentAmt)*15.0F);

            }
        }
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public int getIndentAmt() {
        return indentAmt;
    }
}

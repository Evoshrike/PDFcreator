

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PDFCreator {
    private final String readFilepath;
    private final String destinationPath;

    private List<LineContent> lineContents;








    public PDFCreator(String readFilepath, String destinationPath){
        this.readFilepath = readFilepath;
        this.destinationPath = destinationPath;


    }

    public void createPDF() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(readFilepath));

            lineContents = Parser.parse(lines);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        try {
            PdfWriter pdfWriter = new PdfWriter(destinationPath);
            PdfDocument pdf = new PdfDocument(pdfWriter);
            Document document = new Document(pdf);
            Writer writer = new Writer(lineContents, document);
            writer.write();
            document.close();
        } catch (FileNotFoundException e){
            System.out.println("Error creating file: " + e.getMessage());
        }


    }


    }






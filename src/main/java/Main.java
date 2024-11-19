
public class Main {

    public static void main(String[] args){
        PDFCreator pdfCreator = new PDFCreator("testfile.txt", "testoutput.pdf");
        pdfCreator.createPDF();
    }
}

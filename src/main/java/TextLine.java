
public class TextLine implements LineContent {
    private final String text;

    public TextLine(String s){
        this.text = s;
    }

    public String getText() {
        return text;
    }
}

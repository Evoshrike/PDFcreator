// Parses .txt file and splits the lines into commands and text lines.

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

public class Parser {

    public static Map<String, Function> FunctionsMap = Map.of(".paragraph", Function.PARAGRAPH,
            ".fill", Function.FILL,
            ".bold", Function.BOLD,
            ".italic", Function.ITALIC,
            ".large", Function.LARGE,
            ".normal", Function.NORMAL,
            ".indent", Function.INDENT,
            ".regular", Function.REGULAR,
            ".nofill", Function.NOFILL);
    public List<String> lines;

    public Parser(List<String> strings){
        this.lines = strings;
    }

    public static LinkedList<LineContent> parse(List<String> lines){
        LinkedList<LineContent> lineContents = new LinkedList<>();
        for (String l : lines){
            // Handle command case
            if (l.charAt(0) == '.'){
                if (FunctionsMap.containsKey(l)){
                    FunctionLine fLine = new FunctionLine(FunctionsMap.get(l));
                    lineContents.add(fLine);
                } else {
                    // handle indent case
                    if (l.startsWith(".indent")) {
                        // use regex matching to extract the number
                        String pattern = "\\.indent\\s+(\\d+)";
                        Pattern regex = Pattern.compile(pattern);
                        Matcher matcher = regex.matcher(l);
                        if (matcher.find()){
                            int n = Integer.parseInt(matcher.group(1));
                            FunctionLine fLine = new FunctionLine(Function.INDENT, n);
                            lineContents.add(fLine);
                        } else {
                            throw new IllegalArgumentException("Unable to parse indent argument");
                        }

                    } else {
                        throw new IllegalArgumentException("Unable to recognise command");
                    }
                }

            } else {
                lineContents.add(new TextLine(l));
            }
        }
        return lineContents;
    }
}

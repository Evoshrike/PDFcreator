import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ParserTests {

    @Test
    void testParseText(){
        LinkedList<String> strings = new LinkedList<>();
        // Varied newline characters at the end to test resilience to different styles.
        strings.add("Hello world\r\n");
        strings.add("This is a test\n");

        List<LineContent> l = Parser.parse(strings);

        assertTrue(l.get(0) instanceof TextLine);
        assertEquals(((TextLine) l.get(0)).getText(), "Hello world");
        assertEquals(((TextLine) l.get(1)).getText(), "This is a test");
    }

    @Test
    void testParseCommands(){
        LinkedList<String> strings = new LinkedList<>();
        strings.add(".fill\r\n");
        strings.add(".indent 2\n");

        List<LineContent> l = Parser.parse(strings);

        assertTrue(l.get(0) instanceof FunctionLine);
        assertEquals(((FunctionLine) l.get(0)).getF(), Function.FILL);
        assertEquals(((FunctionLine) l.get(1)).getF(), Function.INDENT);
        assertEquals(((FunctionLine) l.get(1)).getIndentNumber(), 2);
    }

}

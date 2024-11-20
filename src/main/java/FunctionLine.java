

public class FunctionLine implements LineContent {
    private int indentNumber = 0;
    private final Function f;

    // Constructor for all funcs except indent (i.e. no 2nd parameter)
    public FunctionLine(Function f){
        if (f == Function.INDENT) {
            throw new IllegalArgumentException("Indent must come with an associated indent amount");
        } else {
            this.f = f;
        }
    }

    // Constructor for indent - f parameter included for consistency + readability
    public FunctionLine(Function f, int num){
        if (f != Function.INDENT){
            throw new IllegalArgumentException("Only indent is allowed to have a parameter");
        } else {
            this.f = f;
            this.indentNumber = num;
        }
    }

    public Function getF() {
        return f;
    }

    public int getIndentNumber() {
        return indentNumber;
    }
}



public class FunctionLine implements LineContent {
    private int indentAmt = 0;
    private Function f;

    public FunctionLine(Function f){
        if (f == Function.INDENT) {
            throw new IllegalArgumentException("Indent must come with an associated indent amount");
        } else {
            this.f = f;
        }
    }
    public FunctionLine(Function f, int amt){
        if (f != Function.INDENT){
            throw new IllegalArgumentException("Only indent is allowed to have a parameter");
        } else {
            this.f = f;
            this.indentAmt = amt;
        }
    }

    public Function getF() {
        return f;
    }

    public int getIndentAmt() {
        return indentAmt;
    }
}

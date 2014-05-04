package JavaLisp;


public class Token {

    private int start;
    private int end;
    private int line;
    private TokenType type;
    private StringBuffer source;

    public Token(StringBuffer source, int start, int end, int line, TokenType type) {
        this.source = source;
        this.start = start;
        this.end = end;
        this.line = line;
        this.type = type;
    }

    public int getLine() { return line; }
    public int getStart() {
        return start;
    }
    public TokenType getType() {
        return type;
    }
    public int getEnd() {
        return end;
    }
    public StringBuffer getSource() {
        return source;
    }

    public String toSubstring() {
        return this.source.substring(this.start, this.end);
    }

    @Override
    public String toString() {
        return String.format(
            "[@%d, %d-%d] %s",
            this.line,
            this.start,
            this.end,
            this.toSubstring().replace("\n", "\\n"));
    }
}

package JavaLisp;


public class Token {
    private int offset;
    private int type;
    private int length;
    private String source;

    public Token(String source, int offset, int length, int type) {
        this.offset = offset;
        this.type = type;
        this.length = length;
        this.source = source;
    }

    public int getOffset() {
        return offset;
    }

    public int getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public String getSource() {
        return source;
    }
}

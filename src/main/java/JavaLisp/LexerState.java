package JavaLisp;


import java.util.LinkedList;

public class LexerState {
    public final static int PRE_READING_SENTINEL = -3;
    public final static int FIRST_LINE = 1;

    char c = (char)0;
    int offset = PRE_READING_SENTINEL;
    int mark = PRE_READING_SENTINEL;
    int line = FIRST_LINE;
    LexerStates state = LexerStates.Init;
    StringBuffer source = new StringBuffer();
    LinkedList<Token> tokens = new LinkedList<Token>();

    /**
     * Produces a token that captures the current state.  This
     * token added to the internal collection of tokens held by the
     * instance of LexerState.
     *
     * @param type The type of the token that will be captured.
     * @return the token newly added to the token collection.
     */
    public Token capturePoint(TokenType type) {
        Token t = new Token(source, mark, offset + 1, line, type);
        tokens.add(t);
        return t;
    }

    public Token captureMarked(TokenType type) {
        Token t = new Token(source, mark, offset, line, type);
        tokens.add(t);
        return t;
    }

    public int advanceMark() {
        return this.mark = this.offset + 1;
    }

    public int mark() {
        return this.mark = offset;
    }

    @Override
    public String toString() {
        return String.format(
            "[line: %d offset: %d mark: %d char: '%c']",
            this.line,
            this.offset,
            this.mark,
            this.c
        );

    }
}

package JavaLisp;


import java.util.LinkedList;

public class LexerState {
    public final static int PRE_READING_SENTINEL = -3;

    char c = (char)0;
    int offset = PRE_READING_SENTINEL;
    int line = PRE_READING_SENTINEL;
    int mark = PRE_READING_SENTINEL;
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
    public Token capture(TokenType type) {
        Token t = new Token(source, mark, offset, line, type);
        tokens.add(t);
        return t;
    }

    public int mark() {
        return this.mark = offset;
    }
}
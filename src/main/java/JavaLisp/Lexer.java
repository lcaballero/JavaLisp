package JavaLisp;

import java.util.List;

public class Lexer {

    private final SourceFileReader reader;
    private final LexerState state;

    public Lexer(SourceFileReader reader) {
        this.reader = reader;
        this.state = new LexerState();
    }

    public LexerState getState() {
        return state;
    }

    public List<Token> Tokenize(String src) {

        // This is equivalent to an empty source file.
        if (src.length() > 0) {
            state.state = LexerStates.StartedReading;
        }

        int i = 0, n = src.length();
        while (i < n) {
            char c = src.charAt(i);
            state.c = c;
            state.offset = i;
            state.line += (c == '\n' ? 1 : 0);
            state.source.append(c);
            i++;
        }

        return null;
    }

    public void next(LexerState state) {

        char c = state.c;

        switch (state.state) {
            case StartedReading:
                if (Character.isWhitespace(c)) {
                    state.state = LexerStates.ReadWS;
                }
                else if (c == '(') {
                    state.state = LexerStates.ReadingExpressions;
                    state.capture(TokenType.LeftParen);
                }
                break;
            case ReadingExpressions:
                if (c == '(') {
                    state.capture(TokenType.LeftParen);
                }
                else if (c == '"') {
                    state.state = LexerStates.ReadingString;
                }
                else if (c == ')') {
                    state.capture(TokenType.RightParen);
                }
                break;
            case ReadingString:
                break;
            case ReadingNumber:
                break;
            case ReadingComment:
                break;
            default:
                break;
        }
    }
}

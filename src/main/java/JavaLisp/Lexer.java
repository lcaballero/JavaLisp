package JavaLisp;

import java.io.IOException;
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

    public List<Token> Tokenize() throws IOException {

        int i = 0;
        this.state.state = LexerStates.StartedReading;
        this.state.mark = i;

        while (this.reader.hasNext()) {
            char c = this.reader.read();
            state.c = c;
            state.offset = i;
            state.line += (c == '\n' ? 1 : 0);
            state.source.append(c);
            i++;

            next(state);
        }

        return state.tokens;
    }

    public void next(LexerState state) {

        char c = state.c;

        switch (state.state) {
            case StartedReading:
                if (Character.isWhitespace(c)) {
                    state.state = LexerStates.ReadWS;
                    state.mark();
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

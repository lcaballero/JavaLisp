package JavaLisp;

import java.io.IOException;
import java.util.LinkedList;
import static JavaLisp.LexerStates.*;
import static JavaLisp.TokenType.*;
import static java.lang.Character.isDigit;

/**
 * Tokenizes Lisp like text.
 *
 * These are the types of tokens that this lexer will produce:
 *
 * - Symbols
 * - Strings
 * - Numbers
 * - Parens (left and right)
 * - Comments
 */
public class Lexer {

    private final SourceFileReader reader;
    private final LexerState state;

    public Lexer(SourceFileReader reader) {
        this.reader = reader;
        this.state = new LexerState();
    }

    public LexerState getState() { return state; }

    public LinkedList<Token> tokenize() throws IOException {

        int i = 0;
        this.state.state = StartedReading;
        this.state.offset = i;
        this.state.mark();

        while (this.reader.hasNext()) {
            char c = this.reader.read();
            state.c = c;
            state.offset = i;
            state.source.append(c);
            i++;

            next(state, c);

            state.line += (c == '\n' ? 1 : 0);
        }

        return state.tokens;
    }

    public void next(LexerState state, char c) {

        switch (state.state) {
            case StartedReading:
                startReading(state, c);
                break;
            case ReadingExpressions:
                readingExpressions(state, c);
                break;
            case ReadingWS:
                readingWS(state, c);
                break;
            case ReadingString:
                readingString(state, c);
                break;
            case ReadingNumber:
                readingNumber(state, c);
                break;
            case CheckingForDecimalPoint:
                checkingForDecimalPoint(state, c);
                break;
            case CheckingForExponent:
                checkingForExponent(state, c);
                break;
            case ReadingDecimal:
                readingDecimal(state, c);
                break;
            case ReadingExponent:
                readingExponent(state, c);
                break;
            case CheckingExponentSign:
                checkingExponentSign(state, c);
                break;
            case ReadingLineComment:
                readingLineComment(state, c);
                break;
            default:
                break;
        }
    }

    /**
     * Escape Sequence  Description
     * \t               Insert a tab in the text at this point.
     * \b               Insert a backspace in the text at this point.
     * \n               Insert a newline in the text at this point.
     * \r               Insert a carriage return in the text at this point.
     * \f               Insert a form feed in the text at this point.
     * \'               Insert a single quote character in the text at this point.
     * \"               Insert a double quote character in the text at this point.
     * \\               Insert a backslash character in the text at this point.
     */
    public void readingString(LexerState state, char c) {

        switch (c) {
            case '"':
                state.state = ReadingExpressions;
                state.capturePoint(QuotedString);
                state.advanceMark();
                break;
            case '\'':
                state.state = CheckingStringEscapes;
            default:
                break;
        }
    }

    /**
     * Transitions back to reading a string after accepting one of the legal
     * escape characters, which are one of [tbnrf'\"].
     */
    public void checkingStringEscapes(LexerState state, char c) {

        switch (c) {
            case 't':
            case 'b':
            case 'n':
            case 'r':
            case 'f':
            case '\'':
            case '"':
            case '\\':
                //TODO: re-write the source buffer to include raw version of the
                //TODO: underlying value instead of the escape sequence.
                state.state = ReadingString;
                break;
            default:
                throw new IllegalStateException("Didn't find legal escape for: " + c);
        }
    }

    public void checkingForDecimalPoint(LexerState state, char c) {
        readingNumber(state, c);
    }

    /**
     * This method transitions via for this regex:
     *
     * ^[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?$
     */
    public void readingNumber(LexerState state, char c) {

        if (isDigit(c)) {
            state.state = CheckingForDecimalPoint;
            return;
        }

        switch (c) {
            case '.':
                state.state = ReadingDecimal;
                return;
            default:
                state.captureMarked(Number);
                state.mark();
                state.state = ReadingExpressions;
                readUnderPoint(state, c);
                break;
        }
    }

    /**
     * State transition function for reading the [eE] in a floating point
     * number.  Basically, when the lexer has read the integral portion,
     * the decimal, and has started reading the fractional portion, it
     * enters a state where it could eventually see the [eE] for scientific
     * notation.
     */
    public void checkingForExponent(LexerState state, char c) { 
        if (isDigit(c)) {
            return; // Keep reading in numbers
        }

        switch (c) {
            case 'e':
            case 'E':
                state.state = CheckingExponentSign;
                break;
            default:
                state.captureMarked(Number);
                state.mark();
                readUnderPoint(state, c);
                break;
        }
    }

    /**
     * State transitions from having read the [eE] to now optionally
     * reading in the sign of the exponent, which is [+-]{0,1}, and
     * so either the sign is present or the first exponent number is
     * read.
     */
    public void checkingExponentSign(LexerState state, char c) {

        switch (c) {
            case '+':
            case '-':
                state.state = ReadingExponent;
                break;
            default:
                if (isDigit(c)) {
                    state.state = ReadingExponent;
                } else {
                    throw new IllegalStateException("Started exponenet, but didn't provide sign or magnitude");
                }
                break;
        }
    }

    /**
     * Transitions from reading the scientific notation exponent
     * (everything after the [eE] plus sign) to reading some integral
     * value, and then if the integral value terminates transitioning
     * into a viable state.
     */
    public void readingExponent(LexerState state, char c) {

        if (isDigit(c)) {
            // do nothing continue reading magnitude
        } else if (Character.isWhitespace(c)) {
            state.state = ReadingWS;
            state.captureMarked(Number);
            state.mark();
        } else {
            // The allowable values here are '(', ')' or WS?
            state.captureMarked(Number);
            state.mark();
            readUnderPoint(state, c);
        }
    }

    public void readingDecimal(LexerState state, char c) {
        if (isDigit(c)) {
            state.state = CheckingForExponent;
        }
        else {
            throw new IllegalStateException("Expecting number after decimal point");
        }
    }

    public void readingLineComment(LexerState state, char c) {
        switch (c) {
            case '\n':
                state.captureMarked(LineComment);
                state.mark();
                state.state = ReadingWS;
                break;
        }
    }

    public void readingExpressions(LexerState state, char c) {

        if (isDigit(c)) {
            state.state = ReadingNumber;
            state.mark();
            return;
        }

        switch (c) {
            case '(':
                state.capturePoint(LeftParen);
                state.advanceMark();
                break;
            case '"':
                state.state = ReadingString;
                break;
            case ')':
                state.capturePoint(RightParen);
                state.advanceMark();
                break;
            default:
                break;
        }
    }

    public void readUnderPoint(LexerState state, char c) {

        switch (c) {
            case '(':
                state.state = ReadingExpressions;
                state.capturePoint(LeftParen);
                state.mark();
                break;
            case ')':
                state.state = ReadingExpressions;
                state.capturePoint(RightParen);
                state.mark();
                break;
            case ' ':
                state.state = ReadingWS;
                state.mark();
                break;
            default:
                throw new IllegalStateException("Cannot read value under point\n" + this.state.toString());
        }
    }

    public void readingWS(LexerState state, char c) {

        if (Character.isWhitespace(c)) {
            return;
        }

        if (!Character.isWhitespace(c)) {
            state.captureMarked(WS);
            state.mark();
        }

        if (isDigit(c)) {
            state.state = ReadingNumber;
            state.mark();
            return;
        }

        switch (c) {
            case '"':
                state.state = ReadingString;
                state.mark();
                break;
            case ';':
                state.state = ReadingLineComment;
                state.mark();
                break;
            default:
                readUnderPoint(state, c);
                break;
        }
    }

    public void startReading(LexerState state, char c) {

        if (Character.isWhitespace(c)) {
            state.state = ReadingWS;
            // position marked since this is 'start' reading
            return;
        }

        if (isDigit(c)) {
            state.state = ReadingNumber;
            state.mark();
            return;
        }

        switch (c) {
            case '(':
                state.state = ReadingExpressions;
                state.capturePoint(LeftParen);
                state.advanceMark();
                break;
            case '"':
                state.state = ReadingString;
                break;
            case ';':
                state.state = ReadingLineComment;
                state.mark();
                break;
            default:
                break;
        }
    }
}

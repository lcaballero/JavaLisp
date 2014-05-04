package JavaLisp;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static JavaLisp.LexerStates.*;


public class LexToStringTests {

    private Helpers helpers;
    private Lexer lex;
    private LexerState state;

    public void transitionCheck(LexerStates from, char c, LexerStates to, ILexTransition transition) {
        state.state = from;
        transition.transition(state, c);
        assertThat(state.state, is(to));
    }

    @Before
    public void setup() {
        helpers = new Helpers();
        lex = new Lexer(null);
        state = lex.getState();
    }

    @Test
    public void should_transition_from_started_read_with_dq_to_reading_string() {
        transitionCheck(StartedReading, '"', ReadingString, lex::startReading);
    }

    @Test
    public void should_transition_from_reading_expressions_with_dq_to_reading_string() {
        transitionCheck(ReadingExpressions, '"', ReadingString, lex::readingExpressions);
    }

    @Test
    public void should_read_first_string() throws IOException {
        String s = "(\"first string\")";
        Lexer lex = helpers.newLexer(s);
        LinkedList<Token> tokens = lex.tokenize();

        assertThat("For expression: " + s, tokens.size(), is(3));
    }

    @Test
    public void should_read_string_with_escape() throws IOException {
        String a = "\"first\tstring\"";
        String s = "("+a+")";
        Lexer lex = helpers.newLexer(s);
        LinkedList<Token> tokens = lex.tokenize();

        assertThat("For expression: " + s, tokens.size(), is(3));
        assertThat(tokens.get(1).toSubstring(), is(a));
    }

    @Test (expected = IllegalStateException.class)
    public void should_throw_exception_for_illegal_escape() {
        transitionCheck(CheckingStringEscapes, '2', ReadingString, lex::checkingStringEscapes);
    }

    @Test
    public void should_transition_back_to_reading_string() {
        char[] escapes = "tbnrf'\\\"".toCharArray();

        for (char c : escapes) {
            transitionCheck(CheckingStringEscapes, c, ReadingString, lex::checkingStringEscapes);
        }
    }
}

package JavaLisp;


import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LexerTests {

    private Helpers helpers;

    @Before
    public void setup() {
        helpers = new Helpers();
    }

    @Test
    public void should_read_empty_source() throws IOException {
        Lexer lex = helpers.newLexer("");
        List<Token> tokens = lex.Tokenize();

        assertThat(tokens, notNullValue());
        assertThat(tokens.size(), is(0));
    }

    @Test
    public void should_read_empty_list() throws IOException {
        Lexer lex = helpers.newLexer("()");
        List<Token> tokens = lex.Tokenize();

        assertThat(tokens, notNullValue());
        assertThat(tokens.size(), is(2));
    }

    @Test
    public void should_read_empty_list_and_have_lparen_token() throws IOException {
        Lexer lex = helpers.newLexer("()");
        List<Token> tokens = lex.Tokenize();

        Token t = tokens.get(0);

        assertThat("getStart", t.getStart(), is(0));
        assertThat("getEnd", t.getEnd(), is(0));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.LeftParen));


    }

    @Test
    public void should_read_empty_list_and_have_rparen_token() throws IOException {
        Lexer lex = helpers.newLexer("()");
        List<Token> tokens = lex.Tokenize();

        Token t = tokens.get(1);

        assertThat("getStart", t.getStart(), is(1));
        assertThat("getEnd", t.getEnd(), is(1));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.RightParen));
    }
}

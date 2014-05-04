package JavaLisp;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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

        List<Token> tokens = helpers.newLexer("").tokenize();

        assertThat(tokens, notNullValue());
        assertThat(tokens.size(), is(0));
    }

    @Test
    public void should_read_empty_list() throws IOException {

        List<Token> tokens = helpers.newLexer("()").tokenize();
        assertThat(tokens, notNullValue());
        assertThat(tokens.size(), is(2));
    }

    @Test
    public void should_read_empty_list_and_have_lparen_token() throws IOException {

        LinkedList<Token> tokens = helpers.newLexer("()").tokenize();

        assertThat(tokens.size(), is(2));

        Token t = tokens.get(0);

        assertThat("getStart", t.getStart(), is(0));
        assertThat("getEnd", t.getEnd(), is(1));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.LeftParen));
    }

    @Test
    public void should_read_empty_list_and_have_rparen_token() throws IOException {

        Token t = helpers.newLexer("()").tokenize().getLast();

        assertThat("getStart", t.getStart(), is(1));
        assertThat("getEnd", t.getEnd(), is(2));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.RightParen));
        assertThat("toSubstring", t.toSubstring(), is(")"));
    }

    @Test
    public void should_produce_initial_ws_token() throws IOException {

        Token t = helpers.newLexer("  ()").tokenize().get(0);

        assertThat("getStart", t.getStart(), is(0));
        assertThat("getEnd", t.getEnd(), is(2));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.WS));
        assertThat("toSubstring", t.toSubstring(), is("  "));
    }

    @Test
    public void should_ws_then_tokens_token() throws IOException {

        Token t = helpers.newLexer("  ()").tokenize().get(0);

        assertThat("getStart", t.getStart(), is(0));
        assertThat("getEnd", t.getEnd(), is(2));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.WS));
        assertThat("toSubstring", t.toSubstring(), is("  "));
    }

    @Test
    public void should_read_leading_comment() throws IOException {

        Path p = Paths.get("files/comments-1.txt");
        Token t = helpers.newLexer(p).tokenize().get(0);
        String text = helpers.toText(p);

        assertThat("getStart", t.getStart(), is(0));
        assertThat("getEnd", t.getEnd(), is(text.length() - 1));
        assertThat("getLine", t.getLine(), is(1));
        assertThat("getSource", t.getSource(), notNullValue());
        assertThat("getType", t.getType(), is(TokenType.LineComment));
        assertThat("toSubstring", t.toSubstring(), is(text.trim()));
    }

    @Test
    public void should_read_tokens_in_this_order() throws IOException {

        Path p = Paths.get("files/ordered-1.txt");
        List<Token> tokens = helpers.newLexer(p).tokenize();

        ofType(
                tokens,
                TokenType.WS,
                TokenType.LineComment,
                TokenType.WS,
                TokenType.LeftParen,
                TokenType.RightParen);
    }

    public void ofType(List<Token> tokens, TokenType... types) {

        assertThat(tokens.size(), is(types.length));

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            TokenType type = types[i];

            assertThat(t.getType(), is(type));
        }
    }
}

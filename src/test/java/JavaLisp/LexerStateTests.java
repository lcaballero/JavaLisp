package JavaLisp;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LexerStateTests {

    @Before
    public void setup() {}

    @Test
    public void should_have_reasonable_starting_default_values() {
        LexerState state = new LexerState();

        assertThat(state.offset, is(LexerState.PRE_READING_SENTINEL));
        assertThat(state.line, is(LexerState.FIRST_LINE));
        assertThat(state.mark, is(LexerState.PRE_READING_SENTINEL));

        assertThat(state.state, is(LexerStates.Init));
        assertThat(state.source, notNullValue());
        assertThat(state.tokens, notNullValue());
    }

    @Test
    public void should_capture_token_with_current_state() {
        LexerState state = new LexerState();

        int mark = 0;
        state.mark = mark;
        state.offset = 1;
        state.line = 2;
        state.source = new StringBuffer("abc");

        assertThat(state.tokens.size(), is(0));

        Token t = state.capture(TokenType.WS);

        assertThat(t.getStart(), is(mark));
        assertThat(t.getEnd(), is(state.offset));
        assertThat(t.getLine(), is(state.line));
        assertThat(t.getSource(), is(state.source));
        assertThat(t.getType(), is(TokenType.WS));
        assertThat(state.tokens.size(), is(1));
    }

    @Test
    public void should_mark_current_offset_and_persist_value() {
        LexerState state = new LexerState();

        int markPos = 7;
        int lastIndex = 10;

        for (int i = 0; i <= 10; i++) {
            state.offset = i;

            if (i == markPos) {
                int mark = state.mark();
                assertThat(mark, is(state.offset));
            }
        }

        assertThat(state.offset, is(lastIndex));
        assertThat(state.mark, is(markPos));
    }
}

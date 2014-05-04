package JavaLisp;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static JavaLisp.LexerStates.*;


public class LexToFloatTests {

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
    public void should_transition_to_reading_number() {

        LexerState state = lex.getState();
        state.mark = 3;
        state.offset = 42;

        lex.readingExpressions(state, '4');

        assertThat(state.state, is(ReadingNumber));
        assertThat(state.mark, is(state.offset));
    }

    @Test
    public void readingNumber_given_subsequent_numbers_it_should_check_for_decimal_point() {
        transitionCheck(ReadingNumber, '2', CheckingForDecimalPoint, lex::readingNumber);
    }

    @Test
    public void readingNumber_given_decimal_point_begin_reading_decimals() {
        transitionCheck(CheckingForDecimalPoint, '.', ReadingDecimal, lex::readingNumber);
    }

    @Test (expected = IllegalStateException.class)
    public void readingDecimal_expect_at_least_one_digit_afer_decimal() {
        transitionCheck(ReadingDecimal, ' ', CheckingForExponent, lex::readingDecimal);
    }

    @Test
    public void readingNumber_when_checking_for_decimal_point_should_continue_to_check_for_decimal() {
        transitionCheck(CheckingForDecimalPoint, '2', CheckingForDecimalPoint, lex::readingNumber);
    }

    @Test
    public void checkingForExponent_should_change_to_reading_ws_when_ws() {
        transitionCheck(CheckingForExponent, ' ', ReadingWS, lex::checkingForExponent);
    }

    @Test
    public void checkingForExponent_should_change_to_reading_expr_when_lparen() {
        transitionCheck(CheckingForExponent, '(', ReadingExpressions, lex::checkingForExponent);
    }

    @Test
    public void checkingForExponent_should_change_to_reading_expr_when_rparen() {
        transitionCheck(CheckingForExponent, ')', ReadingExpressions, lex::checkingForExponent);
    }

    @Test
    public void readingDecimal_should_check_for_exponent_after_decimal() {
        transitionCheck(ReadingDecimal, '2', CheckingForExponent, lex::readingDecimal);
    }

    @Test
    public void checkingForExponent_should_accept_e_in_exponent() {
        transitionCheck(CheckingForExponent, 'e', CheckingExponentSign, lex::checkingForExponent);
    }

    @Test
    public void checkingForExponent_should_transition_to_reading_expressions_on_non_number_or_exponent() {
        transitionCheck(CheckingForExponent, 'e', CheckingExponentSign, lex::checkingForExponent);
    }

    @Test
    public void checkingExponentSign_should_accept_sign() {
        transitionCheck(CheckingExponentSign, '+', ReadingExponent, lex::checkingExponentSign);
    }

    @Test
    public void checkingExponentSign_should_accept_positive_magnitude() {
        transitionCheck(CheckingExponentSign, '2', ReadingExponent, lex::checkingExponentSign);
    }

    @Test (expected = IllegalStateException.class)
    public void checkingExponentSign_should_throw_exception_for_non_integer_exponent() {
        transitionCheck(CheckingExponentSign, ' ', ReadingExponent, lex::checkingExponentSign);
    }

    @Test
    public void readingExponent_should_accept_digits() {
        transitionCheck(ReadingExponent, '1', ReadingExponent, lex::readingExponent);
    }

    @Test
    public void readingExponent_should_read_ws_at_terminating_with_ws() {
        transitionCheck(ReadingExponent, ' ', ReadingWS, lex::readingExponent);
    }

    @Test (expected = IllegalStateException.class)
    public void readingExponent_should_throw_exception_if_non_floating_point_character_in_exponent() {
        transitionCheck(ReadingExponent, 'w', ReadingWS, lex::readingExponent);
    }

    @Test
    public void should_accept_each_number() throws IOException {
        List<String> numbers = helpers.toNumbers(Paths.get("files/read-number-1.txt"));

        for (String s : numbers) {
            Lexer lex = helpers.newLexer(s);
            LinkedList<Token> tokens = lex.tokenize();

            assertThat("For expression: " + s, tokens.size(), is(3));
        }
    }

    @Test
    public void should_accept_number_1_1901() throws IOException {
        String s = "(1.1901)";
        Lexer lex = helpers.newLexer(s);
        LinkedList<Token> tokens = lex.tokenize();

        assertThat("For expression: " + s, tokens.size(), is(3));
    }

    @Test
    public void should_accept_number_3_14_neg10() throws IOException {
        String s = "(3.14e-10)";
        Lexer lex = helpers.newLexer(s);
        LinkedList<Token> tokens = lex.tokenize();

        assertThat("For expression: " + s, tokens.size(), is(3));
    }
}

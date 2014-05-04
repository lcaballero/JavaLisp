package JavaLisp;

@FunctionalInterface
public interface ILexTransition {
    public void transition(LexerState state, char c);
}

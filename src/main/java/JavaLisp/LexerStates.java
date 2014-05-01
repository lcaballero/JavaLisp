package JavaLisp;


public enum LexerStates {
    Init,
    StartedReading,
    ReadWS,
    ReadingExpressions,
    ReadingString,
    ReadingNumber,
    ReadingComment
}

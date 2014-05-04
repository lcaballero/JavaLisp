package JavaLisp;


public enum LexerStates {
    Init,
    StartedReading,
    ReadingWS,
    ReadingExpressions,
    ReadingString,
    ReadingNumber,
    ReadingLineComment,
    CheckingForDecimalPoint,
    CheckingForExponent,
    ReadingDecimal,
    ReadingExponent,
    CheckingExponentSign,
    CheckingStringEscapes
}

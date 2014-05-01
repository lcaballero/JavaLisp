package JavaLisp;

import java.io.BufferedReader;
import java.io.StringReader;


public class Helpers {

    public SourceFileReader newSourceReader(String s) {
        StringReader reader = new StringReader(s);
        BufferedReader buff = new BufferedReader(reader);
        SourceFileReader src = new SourceFileReader(buff);

        return src;
    }

    public Lexer newLexer(String s) {
        Lexer lex = new Lexer(newSourceReader(s));
        return  lex;
    }
}
